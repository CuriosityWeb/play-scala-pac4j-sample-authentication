package services

import daos.UsersDao
import models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class UserService @Inject() (usersDao: UsersDao) {

  def getUser(email: String): Future[Option[User]] = usersDao.getUser(email)
}
