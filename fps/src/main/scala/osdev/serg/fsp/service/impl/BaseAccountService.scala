package osdev.serg.fsp.service.impl

import osdev.serg.fsp.model.Account
import osdev.serg.fsp.repository.AccountRepository
import osdev.serg.fsp.service.AccountService

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class BaseAccountService(accountRepository: AccountRepository)
                        (implicit val ex: ExecutionContext) extends AccountService {
  override def create(account: Account): Future[Account] = {
    accountRepository.create(account)
  }

  override def delete(id: UUID): Future[Unit] = {
    accountRepository.delete(id)
  }

  override def get(id: UUID): Future[Account] = {
    accountRepository.get(id)
  }

  override def find(id: UUID): Future[Option[Account]] = {
    accountRepository.find(id)
  }

  override def getAll(): Future[Seq[Account]] = {
    accountRepository.getAll()
  }

  override def updateUsername(request: Account): Future[Option[Account]] = {
    accountRepository.updateUsername(request)
  }

  override def putMoney(request: Account): Future[Either[String, Account]] = {
    val accForChange = accountRepository.find(request.id)
    accForChange.flatMap {
        case Some(value) => accountRepository
          .updateBalance(Account(id = request.id, balance = value.balance + request.balance))
          .map {
            case Some(value) => Right(value)
          }
        case None => Future.successful(Left("Счет не найден"))
    }
  }

  override def getMoney(request: Account): Future[Either[String, Account]] = {
    val accForChange = accountRepository.find(request.id)
    accForChange.flatMap {
      case Some(value) =>
        if(value.balance - request.balance < 0) Future.successful(Left("Недостаточно средств на счете"))
        else accountRepository
            .updateBalance(Account(id = request.id, balance = value.balance - request.balance))
            .map {
              case Some(value) => Right(value)
            }
      case None => Future.successful(Left("Счет не найден"))
    }
  }
}
