package models

import util.LocalDateUtils
import play.api.libs.json._

import java.time.LocalDate

object JsonFormats {

  implicit class JsonOps[T](obj: T) {
    def asJson(implicit tjs: Writes[T]): JsValue = Json.toJson(obj)
  }

  implicit lazy val genderJsonFormat: Format[Gender] = Format(
    (json: JsValue) =>
      json
        .validate[String]
        .map(Gender.fromCaseInsensitiveName)
        .flatMap {
          case Some(gender) => JsSuccess(gender)
          case None         => JsError(JsonValidationError(s"Not a valid gender: [$json], valid gender are [Male, Female]."))
        },
    (o: Gender) => JsString(o.toString)
  )

  implicit lazy val localDateJsonFormat: Format[LocalDate] = Format(
    (json: JsValue) =>
      json
        .validate[String]
        .map(LocalDateUtils.parseString)
        .flatMap {
          case Some(date) => JsSuccess(date)
          case None       => JsError(JsonValidationError(s"Not a valid date: [$json], valid date format is [31-12-2022]"))
        },
    (o: LocalDate) => JsString(LocalDateUtils.format(o))
  )

  implicit lazy val userJsonFormat: OFormat[User] = Json.format[User]
  implicit lazy val createUserRequestJsonFormat: OFormat[CreateUserRequest] = Json.format[CreateUserRequest]
}
