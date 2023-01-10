package osdev.serg.fsp.service

import osdev.serg.fsp.model.Transaction

import scala.concurrent.Future

trait TransactionService {
  def create(transaction: Transaction): Future[Either[String,Transaction]]
  def getAll(): Future[Seq[Transaction]]
}
