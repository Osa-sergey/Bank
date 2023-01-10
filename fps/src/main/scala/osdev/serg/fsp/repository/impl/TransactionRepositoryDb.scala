package osdev.serg.fsp.repository.impl

import osdev.serg.fsp.db.TransactionDb.transactionTable
import osdev.serg.fsp.model.Transaction
import osdev.serg.fsp.repository.TransactionRepository
import slick.jdbc.PostgresProfile.api._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class TransactionRepositoryDb (implicit val ex: ExecutionContext, db: Database) extends TransactionRepository {
  override def create(transaction: Transaction): Future[Transaction] = {
    for {
      _ <- db.run(transactionTable += transaction)
      res <- get(transaction.id)
    } yield res
  }

  override def get(id: UUID): Future[Transaction] = {
    db.run {
      transactionTable
        .filter(_.id === id)
        .result
        .head
    }
  }

  override def getAll(): Future[Seq[Transaction]] = {
    db.run(transactionTable.result)
  }
}
