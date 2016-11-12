import org.mongodb.scala.bson.BsonObjectId
import org.bson.types.ObjectId
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection}
import org.mongodb.scala.model.Filters._

class DatabaseInstance(val address: String, val db: String) {

  private val client = MongoClient(address)
  private val database = client.getDatabase(db)

  def getUser(id: ObjectId): String = {
    val collection = database.getCollection("users")

    // Find user with the provided id
    getItemFromDatabase(collection, id)
  }

  def getListing(id: ObjectId): String = {
    val collection = database.getCollection("listings")

    getItemFromDatabase(collection, id)

  }

  def getChat(id: ObjectId): String = {
    val collection = database.getCollection("chats")
    getItemFromDatabase(collection, id)

  }

  def addUser(username: String, password: String, email: String): Option[UserID] = {

    val doc: Document = Document(
      "_id" -> new ObjectId(),
      "username" -> username,
      "password" -> password,
      "email" -> email,
      "listings" -> "[]",// List of listing_ids (personal listings)
      "desired_pets" -> "[]", // List of listing_ids
      "rejected_pets" -> "[]", // List of listing_ids
      "chats" -> "[]" // List of chat_ids
    )
    val collection = database.getCollection("users")
    collection.insertOne(doc).subscribe(
      (blah: Completed) => println(blah)
    )

    var usrID: Option[UserID] = None
    collection.find(equal("username", username)).first().subscribe(
      (user: Document) => user.get("_id") match {
        case None => return None
        case Some(result) =>
          val resultObjectId = result.asObjectId().toString
          println(" >> Result Object Id from query: " + resultObjectId)
          usrID = Some(UserID(new ObjectId(resultObjectId))) // Might need to change, gives back an object id
      }
    )
    usrID
  }

  def addListing(petName: String, listerID: UserID, petInfo: String) = {

  }

  def addChat(lister: UserID, interested: UserID, listing: ListingID) = {

  }

  def getItemFromDatabase(collection: MongoCollection[Document], id: ObjectId): String = {
    println(">> Finding ObjectId: " + id + " from " + collection.namespace)
    var itemJson = ""
    collection.find(equal("_id", id)).first().subscribe(
      (item: Document) => itemJson = item.toJson(), // onNext (success?)
      (error: Throwable) => println(s"Query failed: ${error.getMessage}"), // onError
      () => println("Done") // onComplete
    )
    itemJson
  }

  def closeConnection() = {
    client.close()
  }

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