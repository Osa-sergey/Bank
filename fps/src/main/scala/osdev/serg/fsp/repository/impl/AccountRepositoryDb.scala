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
    db.run {
      accountTable
        .filter(_.id === id)
        .delete
    }.map(_ => ())
  }

  override def get(id: UUID): Future[Account] = {
    db.run {
      accountTable
        .filter(_.id === id)
        .result
        .head
    }
  }

  override def find(id: UUID): Future[Option[Account]] = {
    db.run {
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

  override def putMoney(request: PutMoneyOnAccount): Future[Either[String, Account]] = {
    val accForChange = accountTable
      .filter(_.id === request.id)
      .map(_.balance)
    for {
      oldBalanceOption <- db.run(accForChange.result.headOption)
      incomingSum = request.balance
      updateBalance = oldBalanceOption.map { oldBalance =>
        Right(oldBalance + incomingSum)
      }.getOrElse(Left("Счет не найден"))
      future = updateBalance.map(balance => db.run {
        accForChange.update(balance)
      }) match {
        case Right(future) => future.map(Right(_))
        case Left(s) => Future.successful(Left(s))
      }
      updated <- future
      res <- find(request.id)
    } yield updated.map(_ => res.get)
  }

  override def getMoney(request: GetMoneyFromAccount): Future[Either[String, Account]] = {
    val accForChange = accountTable
      .filter(_.id === request.id)
      .map(_.balance)
    for {
      oldBalanceOption <- db.run(accForChange.result.headOption)
      outgoingSum = request.balance
      updateBalance = oldBalanceOption.map { oldBalance =>
        if ((oldBalance - outgoingSum) < 0)
          Left("Недостаточно денег на счету")
        else Right(oldBalance - outgoingSum)
      }.getOrElse(Left("Счет не найден"))
      future = updateBalance.map(balance => db.run {
        accForChange.update(balance)
      }) match {
        case Right(futute) => futute.map(Right(_))
        case Left(s) => Future.successful(Left(s))
      }
      updated <- future
      res <- find(request.id)
    } yield updated.map(_ => res.get)
  }

}