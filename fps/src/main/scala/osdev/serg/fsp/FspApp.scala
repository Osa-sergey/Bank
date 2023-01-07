package osdev.serg.fsp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import osdev.serg.fsp.route.HealthCheckRoute

object FspApp extends App {
  implicit val system: ActorSystem = ActorSystem("FspApp")

  private val healthCheckRoute = new HealthCheckRoute().route
  Http().newServerAt("0.0.0.0", 8080).bind(healthCheckRoute)
}