package osdev.serg.fsp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import slick.jdbc.PostgresProfile.api._

import osdev.serg.fsp.db.InitDb
import osdev.serg.fsp.repository.impl.AccountRepositoryDb
import osdev.serg.fsp.route.{AccountRoute, HealthCheckRoute}


object FspApp extends App {
  implicit val system: ActorSystem = ActorSystem("FspApp")
  implicit val ex = system.dispatcher
  implicit val db = Database.forConfig("database.postgres")

  new InitDb().prepare()

  private val accountRepository = new AccountRepositoryDb()

  private val healthCheckRoute = new HealthCheckRoute().route
  private val accountRoute = new AccountRoute(accountRepository).route
  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(healthCheckRoute ~ accountRoute)
}