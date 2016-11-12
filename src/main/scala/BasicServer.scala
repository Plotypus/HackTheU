import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn

object BasicServer extends App {
  implicit val actorSystem = ActorSystem("hacktheu-system")
  implicit val actorMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  import akka.http.scaladsl.server.Directives._

  val route = {
    pathEndOrSingleSlash {
      complete("under construction")
    }
  }

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
  println("Server is shut down...")
}
