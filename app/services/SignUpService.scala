package services

import daos.UsersDao
import models.{CreateUserRequest, Credential, User}
import org.springframework.security.crypto.bcrypt.BCrypt

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SignUpService @Inject() (usersDao: UsersDao) {

  def submit(req: CreateUserRequest): Future[Unit] = {
    val user = User(req.email, req.firstName, req.lastName, req.dob, req.gender)
    val salt = BCrypt.gensalt()
    val hash = BCrypt.hashpw(req.password, salt)
    val credential = Credential(req.email, hash)

    usersDao.createNewUser(user, credential)
  }
}
