package controllers

import models.JsonFormats._
import org.pac4j.core.profile.UserProfile
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.mvc.{Action, AnyContent}
import services.UserService
import util.JsonOps._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject() (val controllerComponents: SecurityComponents, service: UserService)(implicit ec: ExecutionContext)
    extends Security[UserProfile] {

  def me: Action[AnyContent] = Secure.async { request =>
    request.profiles.headOption match {
      case Some(userProfile) =>
        service.getUser(userProfile.getId).map {
          case Some(user) => Ok(user.asJson)
          case None       => InternalServerError(message("Something went wrong!"))
        }

      case None => Future.successful(Redirect("/signout"))
    }
  }
}
