package models

sealed trait Gender

object Gender {

  case object Male extends Gender
  case object Female extends Gender

  def values: Set[Gender] = Set(Male, Female)
  def fromName(name: String): Option[Gender] = values.find(_.toString == name)
  def fromCaseInsensitiveName(name: String): Option[Gender] = values.find(_.toString.toLowerCase == name.toLowerCase)
}
