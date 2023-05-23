package security

import daos.UsersDao
import org.pac4j.core.context.WebContext
import org.pac4j.core.context.session.SessionStore
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.credentials.{Credentials, UsernamePasswordCredentials}
import org.pac4j.core.exception.{AccountNotFoundException, BadCredentialsException, CredentialsException, TechnicalException}
import org.pac4j.core.profile.CommonProfile
import org.springframework.security.crypto.bcrypt.BCrypt

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Await, TimeoutException}

@Singleton
class FormAuthenticator @Inject() (usersDao: UsersDao) extends Authenticator {

  private val waitDuration = FiniteDuration(5, TimeUnit.SECONDS)

  override def validate(credentials: Credentials, context: WebContext, sessionStore: SessionStore): Unit =
    Option(credentials)
      .collect { case creds: UsernamePasswordCredentials =>
        val email = Option(creds.getUsername).filter(_.nonEmpty).getOrElse(throw new CredentialsException("Email can not be null or empty."))
        val password = Option(creds.getPassword).filter(_.nonEmpty).getOrElse(throw new CredentialsException("Password can not be null or empty."))

        validatePassword(email, password)

        val profile = new CommonProfile()
        profile.setId(email)
        credentials.setUserProfile(profile)
      }
      .getOrElse(throw new CredentialsException("No credentials found."))

  private def validatePassword(email: String, password: String): Unit = {
    (try
      Await.result(usersDao.getCredentials(email), waitDuration)
    catch {
      case _: TimeoutException => throw new TechnicalException("Timeout")
      case ex: Throwable       => throw ex
    }) match {
      case Some(creds) =>
        if (!BCrypt.checkpw(password, creds.passwordHash))
          throw new BadCredentialsException(s"Email or Password incorrect.")

      case None => throw new AccountNotFoundException(s"Account not found:- $email")
    }
  }
}
