package osdev.serg.fsp.db

import osdev.serg.fsp.model.Transaction
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

object TransactionDb {
  class TransactionTable(tag: Tag) extends Table[Transaction](tag, "transaction") {
    val id = column[UUID]("id", O.PrimaryKey)
    val fromAccount = column[UUID]("fromAccount")
    val toAccount = column[UUID]("toAccount")
    val amount = column[Int]("amount")
    def * = (id, fromAccount, toAccount, amount)<> ((Transaction.apply _).tupled, Transaction.unapply)
  }

  val transactionTable = TableQuery[TransactionTable]
}
