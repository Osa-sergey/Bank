package osdev.serg.fsp.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce._
import io.circe.generic.auto._
import osdev.serg.fsp.model._
import osdev.serg.fsp.service.AccountService

import scala.concurrent.ExecutionContext

class AccountRoute(accService: AccountService)(implicit ex: ExecutionContext) extends FailFastCirceSupport {
  def route: Route =
    path("account") {
      get {
        complete(accService.getAll())
      }
    } ~
      path("account" / JavaUUID) { id =>
        get {
          complete(accService.get(id))
        }
      } ~
      path("account") {
        (post & entity(as[CreateAccount])) { account =>
          val newAccount = Account(username = account.username, balance = account.balance)
          complete(accService.create(newAccount))
        }
      } ~
      path("account" / JavaUUID) { id =>
        delete {
          complete(accService.delete(id))
        }
      } ~
      path("account") {
        (put & entity(as[UpdateAccountUsername])) { account =>
          val newAccount = Account(id = account.id, username = account.username)
          complete(accService.updateUsername(newAccount))
        }
      } ~
      path("account" / "balance" / "get") {
        (put & entity(as[GetMoneyFromAccount])) { getRequest =>
          val newBalance = Account(id = getRequest.id, balance = getRequest.balance)
          onSuccess(accService.getMoney(newBalance)) {
            case Right(value) => complete(value)
            case Left(e) => complete(StatusCodes.NotAcceptable, e)
          }
        }
      } ~
      path("account" / "balance" / "put") {
        (put & entity(as[PutMoneyOnAccount])) { getRequest =>
          val newBalance = Account(id = getRequest.id, balance = getRequest.balance)
          onSuccess(accService.putMoney(newBalance)) {
            case Right(value) => complete(value)
            case Left(e) => complete(StatusCodes.NotAcceptable, e)
          }
        }
      }

}
