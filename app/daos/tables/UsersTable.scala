package daos.tables

import daos.tables.UsersTable.TableName
import models.{Gender, User}
import slick.lifted.ProvenShape

import java.time.LocalDate

trait UsersTable extends Table {

  import profile.api._

  class Users(tag: Tag) extends Table[User](tag, TableName) {

    private implicit def genderMappedColumnType: BaseColumnType[Gender] =
      MappedColumnType.base[Gender, String](
        gender => gender.toString,
        gender => Gender.fromName(gender).head
      )

    def email: Rep[String] = column[String]("EMAIL", O.PrimaryKey)
    def firstName: Rep[String] = column[String]("FIRST_NAME")
    def lastName: Rep[Option[String]] = column[Option[String]]("LAST_NAME")
    def dob: Rep[LocalDate] = column[LocalDate]("DOB")
    def gender: Rep[Gender] = column[Gender]("GENDER")

    override def * : ProvenShape[User] = (email, firstName, lastName, dob, gender) <> (User.tupled, User.unapply)
  }

  protected val users: TableQuery[Users] = TableQuery(new Users(_))
}

object UsersTable {
  val TableName: String = "USERS"
}
