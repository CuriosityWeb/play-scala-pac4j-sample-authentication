package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Try

object LocalDateUtils {

  val pattern: String = "yyyy-MM-dd"

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

  def parseString(date: String): Option[LocalDate] = Try(LocalDate.parse(date, formatter)).toOption

  def format(date: LocalDate): String = formatter.format(date)
}
