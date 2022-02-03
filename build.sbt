val AkkaVersion = "2.6.18"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "akka-tcp-client",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  )