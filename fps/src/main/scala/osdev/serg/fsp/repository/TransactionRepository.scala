package osdev.serg.fsp.repository

import osdev.serg.fsp.model.Transaction

import java.util.UUID
import scala.concurrent.Future

trait TransactionRepository {
  def create(transaction: Transaction): Future[Transaction]
  def get(id: UUID): Future[Transaction]
  def getAll(): Future[Seq[Transaction]]
}
