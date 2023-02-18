package osdev.serg.fsp.repository.impl

import osdev.serg.fsp.db.AccountDb._
import osdev.serg.fsp.model.Account
import osdev.serg.fsp.repository.AccountRepository
import slick.jdbc.PostgresProfile.api._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class AccountRepositoryDb(implicit val ex: ExecutionContext, db: Database) extends AccountRepository {
  override def create(account: Account): Future[Account] = {
    for {
      _ <- db.run(accountTable += account)
      res <- get(account.id)
    } yield res
  }

  override def get(id: UUID): Future[Account] = {
    db.run {
      accountTable
        .filter(_.id === id)
        .result
        .head
    }
  }

  override def delete(id: UUID): Future[Unit] = {
    db.run {
      accountTable
        .filter(_.id === id)
        .delete
    }.map(_ => ())
  }

  override def getAll(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  override def updateUsername(account: Account): Future[Option[Account]] = {
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

  override def find(id: UUID): Future[Option[Account]] = {
    db.run {
      accountTable
        .filter(_.id === id)
        .result
        .headOption
    }
  }

  override def updateBalance(account: Account): Future[Option[Account]] = {
    for {
      _ <- db.run {
        accountTable
          .filter(_.id === account.id)
          .map(_.balance)
          .update(account.balance)
      }
      res <- find(account.id)
    } yield res
  }
}