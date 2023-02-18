package osdev.serg.fsp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import osdev.serg.fsp.db.InitDb
import osdev.serg.fsp.repository.impl.{AccountRepositoryDb, TransactionRepositoryDb}
import osdev.serg.fsp.repository.{AccountRepository, TransactionRepository}
import osdev.serg.fsp.route.{AccountRoute, HealthCheckRoute, TransactionRoute}
import osdev.serg.fsp.service.impl.{BaseAccountService, BaseTransactionService}
import osdev.serg.fsp.service.{AccountService, TransactionService}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContextExecutor


object FspApp extends App {
  implicit val system: ActorSystem = ActorSystem("FspApp")
  implicit val ex: ExecutionContextExecutor = system.dispatcher
  implicit val db: PostgresProfile.backend.Database = Database.forConfig("database.postgres")

  new InitDb().prepare()
  private val accountRepository: AccountRepository = new AccountRepositoryDb()
  private val transactionRepository: TransactionRepository = new TransactionRepositoryDb()
  private val accountService: AccountService = new BaseAccountService(accountRepository)
  private val transactionService: TransactionService = new BaseTransactionService(accountService, transactionRepository)

  private val healthCheckRoute = new HealthCheckRoute().route
  private val accountRoute = new AccountRoute(accountService).route
  private val transactionRoute = new TransactionRoute(transactionService).route
  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(healthCheckRoute ~ accountRoute ~ transactionRoute)
}