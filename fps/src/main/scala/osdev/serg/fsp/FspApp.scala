package osdev.serg.fsp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import osdev.serg.fsp.route.HealthCheckRoute
import slick.jdbc.PostgresProfile.api._

object FspApp extends App {
  implicit val system: ActorSystem = ActorSystem("FspApp")
  implicit val ex = system.dispatcher
  implicit val db = Database.forConfig("database.postgres")

  private val healthCheckRoute = new HealthCheckRoute().route
  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(healthCheckRoute)
}