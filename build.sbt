scalaVersion := "2.12.7"


name := "config"
organization := "com.alab"
version := "0.0.1"


libraryDependencies += "org.typelevel" %% "cats-core" % "1.4.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"


lazy val root = (project in file(".")).
  settings(
    name := "config",
    version := "0.0.1",
    organization := "com.alab",
    scalaVersion := "2.10.6",
    sbtPlugin := true,
    sbtVersion := "1.2.4"
  ).enablePlugins(SbtTwirl)