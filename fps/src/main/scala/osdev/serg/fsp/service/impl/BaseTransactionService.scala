package osdev.serg.fsp.service.impl

import osdev.serg.fsp.model.{Account, Transaction}
import osdev.serg.fsp.repository.TransactionRepository
import osdev.serg.fsp.service.{AccountService, TransactionService}

import scala.concurrent.{ExecutionContext, Future}

class BaseTransactionService (accountService: AccountService, transactionRepository: TransactionRepository)
                             (implicit val ex: ExecutionContext) extends TransactionService {
  override def create(transaction: Transaction): Future[Either[String, Transaction]] = {
    val fromAcc = accountService.find(transaction.fromAccount)
    val toAcc = accountService.find(transaction.toAccount)
    fromAcc.flatMap {
      case Some(from) =>
        toAcc.flatMap {
          case Some(to) =>
            if(from.balance < transaction.amount)
              Future.successful(Left(s"На счете отправителя недостаточно средств"))
            else {
              accountService.getMoney(Account(id = from.id, balance = transaction.amount))
              accountService.putMoney(Account(id = to.id, balance = transaction.amount))
              transactionRepository.create(transaction).map(r => Right(r))
            }
          case None => Future.successful(Left(s"Счета с идентификатором ${transaction.toAccount} не существует"))
        }
      case None => Future.successful(Left(s"Счета с идентификатором ${transaction.fromAccount} не существует"))
    }
  }

  override def getAll(): Future[Seq[Transaction]] = {
    transactionRepository.getAll()
  }
}
