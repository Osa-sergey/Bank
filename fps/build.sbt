ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "misis"

val circeVersion = "0.14.1"
val akkaVersion = "2.6.18"
val akkaHttpVersion = "10.2.7"
val akkaHttpJsonVersion = "1.39.2"
val slickVersion = "3.3.3"
val postgresVersion = "42.3.1"
val logbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    name := "fps",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,

      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

      "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion,

      "com.typesafe.slick" %% "slick" % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
      "org.postgresql" % "postgresql" % postgresVersion,

      "ch.qos.logback" % "logback-classic" % logbackVersion
    )
  )

enablePlugins(JavaAppPackaging)
