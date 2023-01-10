package osdev.serg.fsp.service

import osdev.serg.fsp.model.Account

import java.util.UUID
import scala.concurrent.Future

trait AccountService {
  def create(account: Account): Future[Account]

  def delete(id: UUID): Future[Unit]

  def get(id: UUID): Future[Account]

  def find(id: UUID): Future[Option[Account]]

  def getAll(): Future[Seq[Account]]

  def updateUsername(request: Account): Future[Option[Account]]

  def putMoney(request: Account): Future[Either[String, Account]]

  def getMoney(request: Account): Future[Either[String, Account]]

}
