import org.mongodb.scala.MongoClient

class DatabaseInstance(val address: String, val db: String) {

  private val client = MongoClient("ip")
  private val database = client.getDatabase(db)

  def closeConnection() = {
    client.close()
  }

}

class User() {



}