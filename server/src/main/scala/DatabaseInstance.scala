import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters._

class DatabaseInstance(val address: String, val db: String) {

  private val client = MongoClient(address)
  private val database = client.getDatabase(db)

  def getUser(id: UserID): String = {
    val collection = database.getCollection("users")

    // Find user with the provided id
    getItemFromDatabase(collection, id)
  }

  def getListing(id: ListingID): String = {
    val collection = database.getCollection("listings")

    getItemFromDatabase(collection, id)
  }

  def getChat(id: ChatID): String = {
    val collection = database.getCollection("chats")
    getItemFromDatabase(collection, id)
  }

  def addUser(username: String, password: String) = {

  }

  def addListing(petName: String, listerID: UserID, petInfo: String) = {

  }

  def addChat(lister: UserID, interested: UserID, listing: ListingID) = {

  }

  def getItemFromDatabase(collection: MongoCollection[Document], id: ID): String = {
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

abstract class ID(val id: Int)

case class UserID(override val id: Int) extends ID(id) {}
case class ListingID(override val id: Int) extends ID(id) {}
case class ChatID(override val id: Int) extends ID(id) {}

class User(val username: String, val password: String) {

}

class Listing(val petName: String, val listerID: UserID, val petInfo: String) {

}

class Chat(val lister: UserID, val interested: UserID, val listing: ListingID) {

}