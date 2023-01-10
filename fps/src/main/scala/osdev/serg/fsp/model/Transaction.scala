package osdev.serg.fsp.model

import java.util.UUID

case class Transaction(id: UUID = UUID.randomUUID(), fromAccount: UUID, toAccount: UUID, amount: Int)
case class CreateTransaction(from_account: UUID, to_account: UUID, amount: Int)