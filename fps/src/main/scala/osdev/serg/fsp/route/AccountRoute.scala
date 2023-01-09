package osdev.serg.fsp.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce._
import io.circe.generic.auto._
import osdev.serg.fsp.model.{CreateAccount, GetMoneyFromAccount, PutMoneyOnAccount, UpdateAccountUsername}
import osdev.serg.fsp.repository.AccountRepository

import scala.concurrent.ExecutionContext

class AccountRoute(accRepository: AccountRepository)(implicit ex: ExecutionContext) extends FailFastCirceSupport {
  def route =
    path("account") {
      get {
        complete(accRepository.getAll())
      }
    } ~
      path("account" / JavaUUID) { id =>
        get {
          complete(accRepository.get(id))
        }
      } ~
      path("account") {
        (post & entity(as[CreateAccount])) { newAcc =>
          complete(accRepository.create(newAcc))
        }
      } ~
      path("account" / JavaUUID) { id =>
        delete {
          complete(accRepository.delete(id))
        }
      } ~
      path("account") {
        (put & entity(as[UpdateAccountUsername])) { newAcc =>
          complete(accRepository.updateUsername(newAcc))
        }
      } ~
      path("account" / "balance" / "get") {
        (put & entity(as[GetMoneyFromAccount])) { getRequest =>
          onSuccess(accRepository.getMoney(getRequest)) {
            case Right(value) => complete(value)
            case Left(e) => complete(StatusCodes.NotAcceptable, e)
          }
        }
      } ~
      path("account" / "balance" / "put") {
        (put & entity(as[PutMoneyOnAccount])) { getRequest =>
          onSuccess(accRepository.putMoney(getRequest)) {
            case Right(value) => complete(value)
            case Left(e) => complete(StatusCodes.NotAcceptable, e)
          }
        }
      }

}
