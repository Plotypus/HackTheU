name := "HackTheU"

version := "1.0"

scalaVersion := "2.11.8"

val sprayVersion = "1.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.1",
  "io.spray" %% "spray-can" % sprayVersion,
  "io.spray" %% "spray-routing" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
  "io.spray" %% "spray-json" % "1.3.1"
)
