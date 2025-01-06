ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "inter",
    libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.19"
  )
