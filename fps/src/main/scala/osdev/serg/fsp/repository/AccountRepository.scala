package osdev.serg.fsp.repository

import osdev.serg.fsp.model.{Account, CreateAccount, GetMoneyFromAccount, PutMoneyOnAccount, UpdateAccountUsername}

import java.util.UUID
import scala.concurrent.Future

trait AccountRepository {
  def create(account: CreateAccount): Future[Account]
  def delete(id: UUID): Future[Unit]
  def get(id: UUID): Future[Account]
  def find(id: UUID): Future[Option[Account]]
  def getAll(): Future[Seq[Account]]
  def updateUsername(account: UpdateAccountUsername): Future[Option[Account]]
  def putMoney(account: PutMoneyOnAccount): Future[Either[String, Account]]
  def getMoney(account: GetMoneyFromAccount): Future[Either[String, Account]]

}
