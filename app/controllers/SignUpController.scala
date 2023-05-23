package controllers

import models.CreateUserRequest
import models.FormFormats._
import play.api.Logging
import play.api.data.Form
import play.api.mvc._
import services.SignUpService
import util.JsonOps._

import java.sql.SQLIntegrityConstraintViolationException
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Singleton
class SignUpController @Inject() (
  val controllerComponents: ControllerComponents,
  parsers: PlayBodyParsers,
  service: SignUpService
)(implicit ec: ExecutionContext)
    extends BaseController
    with Logging {

  def submit: Action[CreateUserRequest] =
    Action.async(formParser) { request =>
      service
        .submit(request.body)
        .transform {
          case Success(_)                                           => Success(Ok(message("Created.")))
          case Failure(_: SQLIntegrityConstraintViolationException) => Success(BadRequest(errorMessage("Email already exist.")))
          case Failure(exception) =>
            logger.error(s"Error processing request for user with email ${request.body.email}", exception)
            Success(InternalServerError(errorMessage("Unexpected error.")))
        }
    }

  private def formParser: BodyParser[CreateUserRequest] =
    parsers.form(form = signUpForm, onErrors = (form: Form[_]) => BadRequest(errorsJson(form)))
}
