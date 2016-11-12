import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.bson.types.ObjectId
import org.mongodb.scala.{MongoClient, MongoDatabase}

import scala.io.StdIn

object BasicServer {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      throw new IllegalArgumentException("Expected 2 arguments. Got: " + args.length)
    }

    val databaseIP = args(0)
    val database = args(1)
    val mongoDatabase = new DatabaseInstance(databaseIP, database)

    implicit val actorSystem = ActorSystem("hacktheu-system")
    implicit val actorMaterializer = ActorMaterializer()

    mongoDatabase.addUser("newuser", "newpassword", "newemail") match {
      case None => ()
      case Some(UserID(id)) => mongoDatabase.getUser(id)
    }
    //println(new ObjectId("58274c647326944f0c8a0cea") == new ObjectId("58274c647326944f0c8a0cea"))
    //println(mongoDatabase.getUser("58274c647326944f0c8a0cea"))

    val interface = "localhost"
    val port = 8080

    import akka.http.scaladsl.server.Directives._

    val route = {
      pathEndOrSingleSlash {
        complete("under construction")
      } ~
      path("users" / IntNumber) { userID =>
        pathEnd {
          get {
            complete("users / " + userID)
          }
        }
      } ~
      path("pets" / IntNumber) { userID =>
        pathEnd {
          get {
            complete("pets / " + userID)
          }
        }
      }
    }

    val binding = Http().bindAndHandle(route, interface, port)
    println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
    StdIn.readLine()

    import actorSystem.dispatcher
    mongoDatabase.closeConnection()
    binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
    println("Server is shut down...")
  }

}


