package osdev.serg.fsp.model

import java.util.UUID

case class Account(id: UUID = UUID.randomUUID(), username: String, balance: Int)
case class CreateAccount(username: String, balance: Int = 0)
case class UpdateAccountUsername(id: UUID, username: String)
case class PutMoneyOnAccount(id: UUID, balance: Int)
case class GetMoneyFromAccount(id: UUID, balance: Int)
