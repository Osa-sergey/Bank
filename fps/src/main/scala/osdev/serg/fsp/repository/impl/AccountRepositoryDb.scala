package osdev.serg.fsp.repository.impl

import osdev.serg.fsp.db.AccountDb._
import osdev.serg.fsp.model.{Account, CreateAccount, GetMoneyFromAccount, PutMoneyOnAccount, UpdateAccountUsername}
import osdev.serg.fsp.repository.AccountRepository
import slick.jdbc.PostgresProfile.api._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class AccountRepositoryDb(implicit val ex: ExecutionContext, db: Database) extends AccountRepository {
  override def create(createAccount: CreateAccount): Future[Account] = {
    val account = Account(username = createAccount.username, balance = createAccount.balance)
    for {
      _ <- db.run(accountTable += account)
      res <- get(account.id)
    } yield res
  }

  override def delete(id: UUID): Future[Unit] = {
    db.run{
      accountTable
        .filter(_.id === id)
        .delete
    }.map(_ => ())
  }

  override def get(id: UUID): Future[Account] = {
    db.run{
      accountTable
        .filter(_.id === id)
        .result
        .head
    }
  }

  override def find(id: UUID): Future[Option[Account]] = {
    db.run{
      accountTable
        .filter(_.id === id)
        .result
        .headOption
    }
  }

  override def getAll(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  override def updateUsername(account: UpdateAccountUsername): Future[Option[Account]] = {
    for {
      _ <- db.run {
        accountTable
          .filter(_.id === account.id)
          .map(_.username)
          .update(account.username)
      }
      res <- find(account.id)
    } yield res
  }

  override def putMoney(account: PutMoneyOnAccount): Future[Either[String, Account]] = ???

  override def getMoney(account: GetMoneyFromAccount): Future[Either[String, Account]] = ???
}
