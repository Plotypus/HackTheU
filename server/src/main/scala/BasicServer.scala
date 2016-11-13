import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.bson.types.ObjectId

import scala.concurrent.duration._
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.routing.HttpService

import scala.concurrent.{Await, Future}
import scala.collection.mutable
import spray.json._

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
    path("listings/near" / Segment) { userId =>
      get {
        complete(db().getListingsInUserLocation(userId).toString) // Get list of listings of pets in same area of the user
      }
    } ~
    path("listings/interested" / Segment) { userId =>
      get {
        complete(db().getInterestedListingsFromUser(userId))  
      }
    } ~
    path("listings/interested" / Segment / Segment) { (userId, listingId) =>
      post {
        complete("")
      }
    } ~
    path("listing" / Segment) { userId =>
      post {
        entity(as[ListingInfo]) { listingInfo =>
          complete(db().)
        }
      }
    } ~
    path("register") {
      post {
        entity(as[UserInfo]) { userInfo =>
          complete(db().registerAndGetId(userInfo.username, userInfo.password, userInfo.location))
        }
      }
    } ~
    path("login") {
      post {
        entity(as[LoginInfo]) { loginInfo =>
          db().login(loginInfo.username, loginInfo.password) match {
            case Some(id: String) => complete(id)
            case None => complete("")
          }
        }
      }
    }
  }
}

case class UserInfo(username: String, password: String, location: String)
case class LoginInfo(username: String, password: String)
case class ListingInfo(name: String, species: String, age: String, breed: String, weight: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val userInfoFormat = jsonFormat3(UserInfo)
  implicit val loginInfoFormat = jsonFormat2(LoginInfo)
  implicit val listingInfoFormat = jsonFormat5(ListingInfo)
}


