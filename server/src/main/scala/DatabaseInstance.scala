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

  def getUniqueFromCollectionWithAttributeById(collection: MongoCollection[Document], attribute: String, id: String) = {
    Await.result(collection.find(equal(attribute, objectIdForString(id))).first().head(), atMostDuration)
  }

  def getAllFromCollectionWithAttributeById(collection: MongoCollection[Document], attribute: String, id: String) = {
    Await.result(collection.find(equal(attribute, objectIdForString(id))).toFuture(), atMostDuration)
  }

  def getUser(id: String) = {
    getUniqueFromCollectionWithAttributeById(users, "_id", id)
  }

  def getListing(id: String) = {
    getUniqueFromCollectionWithAttributeById(listings, "_id", id)
  }

  def getChat(id: String) = {
    getUniqueFromCollectionWithAttributeById(chats, "_id", id)
  }

  def getListingsForUser(listerId: String) = {
    getAllFromCollectionWithAttributeById(listings, "lister", listerId)
  }

  def addUser(username: String, password: String, email: String): String = {

    val original_id = new ObjectId()
    val id = original_id.toString

    val doc: Document = Document(
      "_id" -> id,
      "username" -> username,
      "password" -> password,
      "email" -> email,
      "listings" -> "[]", // List of listing_ids (personal listings)
      "desired_pets" -> "[]", // List of listing_ids
      "rejected_pets" -> "[]", // List of listing_ids
      "chats" -> "[]" // List of chat_ids
    )
    val collection = database.getCollection("users")
    collection.insertOne(doc).subscribe(
      (blah: Completed) => println(blah)
    )

    id
  }

  def addListing(petName: String, listerID: UserID, petInfo: String) = {

  }

  def addChat(lister: UserID, interested: UserID, listing: ListingID) = {

  }
//
//  def getItemFromDatabase(collection: MongoCollection[Document], id: String): String = {
//    println(">> Finding ObjectId: " + id + " from " + collection.namespace)
//    var itemJson = ""
//    collection.find(equal("_id", id)).first().subscribe(
//      (item: Document) => itemJson = item.toJson(), // onNext (success?)
//      (error: Throwable) => println(s"Query failed: ${error.getMessage}"), // onError
//      () => println("Done") // onComplete
//    )
//    itemJson
//  }

  def closeConnection() = {
    client.close()
  }

//  def listingFromDocument(document: Document): Listing = {
//    val attributes = List("_id", "lister", "name", "species", "age", "breed", "weight", "interested_users", "location")
//    val values = attributes.map(document.get)
//    if (values.contains(None)) {
//      println("[ERROR] Listing missing needed value")
//    } else {
//      new Listing(values(2))
//    }
//  }
}

abstract class ID(val id: ObjectId)

case class UserID(override val id: ObjectId) extends ID(id) {}
case class ListingID(override val id: ObjectId) extends ID(id) {}
case class ChatID(override val id: ObjectId) extends ID(id) {}

class User(val username: String, val password: String) {

}

class Listing(val petName: String, val listerID: UserID, val petInfo: String) {

}

class Chat(val lister: UserID, val interested: UserID, val listing: ListingID) {

}