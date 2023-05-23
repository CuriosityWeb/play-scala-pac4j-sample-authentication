package models

import java.time.LocalDate

case class User(
  email: String,
  firstName: String,
  lastName: Option[String],
  dob: LocalDate,
  gender: Gender
)

case class CreateUserRequest(
  email: String,
  firstName: String,
  lastName: Option[String],
  dob: LocalDate,
  gender: Gender,
  password: String
)
