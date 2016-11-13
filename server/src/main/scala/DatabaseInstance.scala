import org.bson.BsonElement
import org.mongodb.scala.bson.{BsonArray, BsonObjectId}
import org.bson.types.ObjectId
import org.bson.conversions._
import scala.concurrent.duration._
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection}
import org.mongodb.scala.model.Filters._

import scala.collection.mutable
import scala.concurrent.Await

class DatabaseInstance(val address: String, val db: String) {

  private val client = MongoClient(address)
  private val database = client.getDatabase(db)
  private val users = database.getCollection("users")
  private val listings = database.getCollection("listings")
  private val chats = database.getCollection("chats")

  private val atMostDuration = 1 second

  private def objectIdForString(s: String): ObjectId = {
    new ObjectId(s)
  }

  def getUniqueFromCollectionWithAttributeValue(collection: MongoCollection[Document], attribute: String, value: Any) = {
    Await.result(collection.find(equal(attribute, value)).first().head(), atMostDuration)
  }

  def getAllFromCollectionWithAttributeValue(collection: MongoCollection[Document], attribute: String, value: Any) = {
    Await.result(collection.find(equal(attribute, value)).toFuture(), atMostDuration)
  }

  def getAttributeAsStringFromDocument(document: Document, attribute: String): String = {
    document.get(attribute) match {
      case Some(result) => result.asString().getValue
      case None => ""
    }
  }

  def getUser(id: String) = {
    getUniqueFromCollectionWithAttributeValue(users, "_id", objectIdForString(id))
  }

  def getListing(id: String) = {
    getUniqueFromCollectionWithAttributeValue(listings, "_id", objectIdForString(id))
  }

  def getChat(id: String) = {
    getUniqueFromCollectionWithAttributeValue(chats, "_id", objectIdForString(id))
  }

  def getListingsForUser(listerId: String) = {
    getAllFromCollectionWithAttributeValue(listings, "lister", listerId)
  }

  def getListingsInUserLocation(userId: String) = {
    val user = getUser(userId)
    val location = getAttributeAsStringFromDocument(user, "location")
    getAllFromCollectionWithAttributeValue(listings, "location", location)
  }

  def addUserToListing(userId: String, listingId: String) = {
    val listing = getListing(listingId)
    val arr = listing.get("interested_users") match {
      case Some(result) =>
        result.asArray().add(BsonObjectId(userId))
        result
      case None => throw new Exception("Couldn't find interested_users for listing")
    }
    listings.updateOne(equal("_id", listingId), Document("$set" -> Document("interested_users" -> arr)))
  }

  def addListingToUserInterests(listingId: String, userId: String) = {
    val user = getUser(userId)

    val arr = user.get("desired_pets") match {
      case Some(result) =>
        result.asArray().add(BsonObjectId(listingId))
        result
      case None => throw new Exception("Couldn't find desired_pets for user")
    }
    users.updateOne(equal("_id", objectIdForString(userId)), Document("$set" -> Document("desired_pets" -> arr)))
  }

  def createListing(userId: String, name: String, species: String, age: String, breed: String, weight: String) = {
    println("createListing")
    val user = getUser(userId)
    println("get location")
    val location = getAttributeAsStringFromDocument(user, "location")
    println("generating id")
    val original_id = new ObjectId()
    val id = original_id.toString
    println(s"id generated: $id")

    println("creating document")

    val doc: Document = Document(
      "_id" -> id,
      "lister" -> objectIdForString(userId),
      "name" -> name,
      "species" -> species,
      "age" -> age,
      "breed" -> breed,
      "weight" -> weight,
      "location" -> location,
      "interested_users" -> BsonArray()
    )

    println("adding document")
    Await.result(listings.insertOne(doc).toFuture(), atMostDuration)

    id
  }

  def registerAndGetId(username: String, password: String, location: String): String = {

    println(s"register request: $username, $password, $location")

    //TODO: This should probably be checked... because we might get collisions, unless they are guaranteed unique.
    val original_id = new ObjectId()
    val id = original_id.toString

    val doc: Document = Document(
      "_id" -> id,
      "username" -> username,
      "password" -> password,
      "location" -> location,
      "listings" -> BsonArray(),      // List of listing_ids (personal listings)
      "desired_pets" -> BsonArray(),  // List of listing_ids
      "rejected_pets" -> BsonArray(), // List of listing_ids
      "chats" -> BsonArray()          // List of chat_ids
    )
    Await.result(listings.insertOne(doc).toFuture(), atMostDuration)

    id
  }

  def login(username: String, password: String): Option[String] = {
    val user = getUniqueFromCollectionWithAttributeValue(users, "username", username)
    val userpass = getAttributeAsStringFromDocument(user, "password")
    if (userpass == password) {
      Some(getAttributeAsStringFromDocument(user, "_id"))
    } else {
      None
    }
  }

  def getInterestedListingsFromUser(userId: String) = {
    val user = getUser(userId)
    user.get("desired_pets")
  }

  def closeConnection() = {
    client.close()
  }
}
