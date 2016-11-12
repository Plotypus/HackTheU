import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Filters._

class DatabaseInstance(val address: String, val db: String) {

  private val client = MongoClient("ip")
  private val database = client.getDatabase(db)

  def getUser(id: UserID): User = {
    val collection = database.getCollection("users")
    collection.find(equal("_id", id)).first().printHeadResult() // Figure out why this doesn't work
  }

  def getListing(id: ListingID): Listing = {
    val collection = database.getCollection("listings")
    new Listing()
  }

  def getChat(id: ChatID): Chat = {
    val collection = database.getCollection("chats")
    new Chat()
  }

  def addUser(username: String, password: String) = {

  }

  def addListing(petName: String, listerID: UserID, petInfo: String) = {

  }

  def addChat(lister: UserID, interested: UserID, listing: ListingID) = {

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