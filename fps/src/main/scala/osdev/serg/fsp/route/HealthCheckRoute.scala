package osdev.serg.fsp.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class HealthCheckRoute {
  def route: Route =
    (path("health") & get) {
      complete("Healthy")
  }
}
