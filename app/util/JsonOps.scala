package util

import play.api.libs.json.{JsObject, JsString}

object JsonOps {

  def message(msg: String): JsObject = JsObject(Map("message" -> JsString(msg)))

  def errorMessage(msg: String): JsObject = JsObject(Map("error" -> JsString(msg)))
}
