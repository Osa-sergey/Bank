package osdev.serg.fsp.db

import osdev.serg.fsp.model.Account
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

object AccountDb {
  val accountTable = TableQuery[AccountTable]

  class AccountTable(tag: Tag) extends Table[Account](tag, "account") {
    val id = column[UUID]("id", O.PrimaryKey)
    val username = column[String]("username")
    val balance = column[Int]("balance")

    def * = (id, username, balance) <> ((Account.apply _).tupled, Account.unapply)
  }
}
