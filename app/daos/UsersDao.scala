package daos

import com.google.inject.ImplementedBy
import daos.impls.UsersDaoImpl
import models.{Credential, User}

import scala.concurrent.Future

@ImplementedBy(classOf[UsersDaoImpl])
trait UsersDao {

  def getCredentials(email: String): Future[Option[Credential]]

  def getUser(email: String): Future[Option[User]]

  def isUserExist(email: String): Future[Boolean]

  def createNewUser(user: User, credential: Credential): Future[Unit]
}
