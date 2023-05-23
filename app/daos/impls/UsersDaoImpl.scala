package daos.impls

import daos.UsersDao
import daos.tables.{CredentialsTable, UsersTable}
import models.{Credential, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class UsersDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends UsersDao
    with UsersTable
    with CredentialsTable
    with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def getCredentials(email: String): Future[Option[Credential]] =
    db.run(credentials.filter(_.email === email).result.headOption)

  override def getUser(email: String): Future[Option[User]] =
    db.run(users.filter(_.email === email).result.headOption)

  override def isUserExist(email: String): Future[Boolean] =
    db.run(users.filter(_.email === email).exists.result)

  override def createNewUser(user: User, credential: Credential): Future[Unit] =
    db.run(
      DBIO
        .seq(
          users += user,
          credentials += credential
        )
        .transactionally
    )
}
