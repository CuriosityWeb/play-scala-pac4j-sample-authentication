package models

import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.{Form, FormError}
import play.api.libs.json._
import _root_.util.LocalDateUtils

object FormFormats {

  implicit lazy val genderFormatter: Formatter[Gender] =
    new Formatter[Gender] {
      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Gender] =
        data
          .get(key)
          .toRight(Seq(FormError(key, "error.required")))
          .map(Gender.fromCaseInsensitiveName)
          .flatMap(_.toRight(Seq(FormError(key, "error.gender.incorrect"))))

      override def unbind(key: String, value: Gender): Map[String, String] = Map(key -> value.toString)
    }

  val signUpForm: Form[CreateUserRequest] =
    Form(
      mapping(
        "email" -> email,
        "firstName" -> text(5, 100),
        "lastName" -> optional(text),
        "dob" -> localDate(LocalDateUtils.pattern),
        "gender" -> of[Gender],
        "password" -> text(8, 100)
      )(CreateUserRequest.apply)(CreateUserRequest.unapply)
    )

  def errorsJson(form: Form[_]): JsObject =
    JsObject(
      Map(
        "errors" -> JsArray(
          form.errors
            .map(error => JsObject(Map(error.key -> JsArray(error.messages.map(JsString)))))
        )
      )
    )
}
