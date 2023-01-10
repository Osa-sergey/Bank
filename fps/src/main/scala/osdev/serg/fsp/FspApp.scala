package osdev.serg.fsp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import slick.jdbc.PostgresProfile.api._
import osdev.serg.fsp.db.InitDb
import osdev.serg.fsp.repository.AccountRepository
import osdev.serg.fsp.repository.impl.AccountRepositoryDb
import osdev.serg.fsp.route.{AccountRoute, HealthCheckRoute}
import osdev.serg.fsp.service.AccountService
import osdev.serg.fsp.service.impl.BaseAccountService
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContextExecutor


object FspApp extends App {
  implicit val system: ActorSystem = ActorSystem("FspApp")
  implicit val ex: ExecutionContextExecutor = system.dispatcher
  implicit val db: PostgresProfile.backend.Database = Database.forConfig("database.postgres")

  new InitDb().prepare()
  private val accountRepository: AccountRepository = new AccountRepositoryDb()
  private val accountService: AccountService = new BaseAccountService(accountRepository)

  private val healthCheckRoute = new HealthCheckRoute().route
  private val accountRoute = new AccountRoute(accountService).route
  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(healthCheckRoute ~ accountRoute)
}