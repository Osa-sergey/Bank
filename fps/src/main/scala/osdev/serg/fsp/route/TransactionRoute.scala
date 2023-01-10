package osdev.serg.fsp.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce._
import io.circe.generic.auto._
import osdev.serg.fsp.model.{CreateTransaction, Transaction}
import osdev.serg.fsp.service.TransactionService

import scala.concurrent.ExecutionContext

class TransactionRoute(transService: TransactionService)
                      (implicit ex: ExecutionContext) extends FailFastCirceSupport {
  def route: Route = {
    path("transaction") {
      get {
        complete(transService.getAll())
      }
    } ~
      path("transaction") {
        (post & entity(as[CreateTransaction])) { request =>
          val newTransaction = Transaction(fromAccount = request.from_account, toAccount = request.to_account, amount = request.amount)
          onSuccess(transService.create(newTransaction)) {
            case Right(value) => complete(value)
            case Left(e) => complete(StatusCodes.NotAcceptable, e)
          }
        }
      }
  }
}
