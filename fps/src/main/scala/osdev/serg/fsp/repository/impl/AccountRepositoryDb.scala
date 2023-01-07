package osdev.serg.fsp.repository.impl

import osdev.serg.fsp.model.{Account, CreateAccount, GetMoneyFromAccount, PutMoneyOnAccount, UpdateAccountUsername}
import osdev.serg.fsp.repository.AccountRepository
import slick.jdbc.PostgresProfile.api._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class AccountRepositoryDb(implicit val ex: ExecutionContext, db: Database) extends AccountRepository {
  override def create(account: CreateAccount): Future[Account] = ???

  override def delete(id: UUID): Future[Unit] = ???

  override def get(id: UUID): Future[Option[Account]] = ???

  override def getAll(): Future[List[Account]] = ???

  override def updateUsername(account: UpdateAccountUsername): Future[Option[Account]] = ???

  override def putMoney(account: PutMoneyOnAccount): Future[Either[String, Account]] = ???

  override def getMoney(account: GetMoneyFromAccount): Future[Either[String, Account]] = ???
}
