scalaVersion := "2.12.7"


name := "config"
organization := "com.alab"
version := "0.0.1"


libraryDependencies += "org.typelevel" %% "cats-core" % "1.4.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"


val scalaVersionTest = taskKey[Unit]("The version of Scala used for building.")

lazy val library = (project in file("library"))
  .settings(
    scalaVersionTest := {
      println("FuckU")
    }
  )