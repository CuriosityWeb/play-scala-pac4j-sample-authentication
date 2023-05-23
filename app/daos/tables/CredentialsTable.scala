package daos.tables

import daos.tables.CredentialsTable.TableName
import models.{Credential, User}
import slick.lifted.{ForeignKeyQuery, ProvenShape}

trait CredentialsTable extends Table with UsersTable {

  import profile.api._

  class Credentials(tag: Tag) extends Table[Credential](tag, TableName) {

    def email: Rep[String] = column[String]("EMAIL", O.PrimaryKey)
    def passwordHash: Rep[String] = column[String]("PASSWORD_HASH")

    def user: ForeignKeyQuery[Users, User] = foreignKey("FK__USERS_CREDENTIALS", email, users)(_.email, onUpdate = ForeignKeyAction.Cascade)

    override def * : ProvenShape[Credential] = (email, passwordHash) <> (Credential.tupled, Credential.unapply)
  }

  protected def credentials: TableQuery[Credentials] = TableQuery(new Credentials(_))
}

object CredentialsTable {
  val TableName: String = "CREDENTIALS"
}
