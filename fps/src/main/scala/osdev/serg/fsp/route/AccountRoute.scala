package osdev.serg.fsp.route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce._
import io.circe.generic.auto._

import osdev.serg.fsp.model.{CreateAccount, UpdateAccountUsername}
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
      }

}
