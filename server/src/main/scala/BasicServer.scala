import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.bson.types.ObjectId

import scala.concurrent.duration._
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing.HttpService

import scala.concurrent.{Await, Future}
import scala.collection.mutable

object BasicServer {
  var mongoDatabase: Option[DatabaseInstance] = None

  def main(args: Array[String]): Unit = {
    mongoDatabase = Some(new DatabaseInstance(args(0), args(1)))
  }

  val interface = "localhost"
  val port = 8080

  implicit val actorSystem = ActorSystem("hacktheu-system")

  implicit val timeout = Timeout(5.seconds)

  val routeActor = actorSystem.actorOf(Props[RouteActor])

  IO(Http) ? Http.Bind(routeActor, interface, port)
}

class RouteActor extends Actor with HttpService {
  def db(): DatabaseInstance = {
    BasicServer.mongoDatabase match {
      case Some(database) => database
      case None => throw new Exception("no database")
    }
  }

  def receive = runRoute(route)

  def actorRefFactory = context

  val route = {
    pathEndOrSingleSlash {
      complete("under construction")
    } ~
    path("listings" / Segment) { userId =>
      get {
        complete(db().getListingsForUser(userId).toString)
      }
    } ~
    path("users" / Segment) { userID =>
      pathEnd {
        get {
          complete("users / " + userID)
        }
      }
    } ~
    path("pets" / Segment) { userID =>
      pathEnd {
        get {
          complete("pets / " + userID)
        }
      }
    }
  }
}


