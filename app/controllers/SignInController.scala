package controllers

import org.pac4j.core.profile.UserProfile
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.Logging
import play.api.libs.json.{JsArray, JsObject, JsString}
import play.api.mvc.{Action, AnyContent}
import util.JsonOps._

import javax.inject.{Inject, Singleton}

@Singleton
class SignInController @Inject() (val controllerComponents: SecurityComponents) extends Security[UserProfile] with Logging {

  def handleSignIn(error: Option[String]): Action[AnyContent] =
    Action { _ =>
      error match {
        case Some(e) => Unauthorized(errorMessage(e))
        case None =>
          Ok(
            JsObject(
              Map(
                "loginUrl" -> JsString("/callback?client_name=FormClient"),
                "loginRequestMethod" -> JsString("POST"),
                "loginRequestContentType" -> JsString("application/x-www-form-urlencoded"),
                "loginRequestKeys" -> JsArray(Seq(JsString("email"), JsString("password")))
              )
            )
          )
      }
    }

  def successSignIn(): Action[AnyContent] = Secure(request =>
    request.profiles.headOption match {
      case Some(_) => Ok(message("Authenticated"))
      case None    => Redirect("/signout")
    }
  )
}
