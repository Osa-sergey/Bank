package osdev.serg.fsp.db

import osdev.serg.fsp.db.AccountDb.accountTable
import osdev.serg.fsp.db.TransactionDb.transactionTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class InitDb(implicit val ex: ExecutionContext, db: Database) {
  def prepare(): Future[_] = {
    db.run(accountTable.schema.createIfNotExists)
    db.run(transactionTable.schema.createIfNotExists)
  }
}
