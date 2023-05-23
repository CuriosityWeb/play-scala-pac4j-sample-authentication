package security

import com.google.inject.{AbstractModule, Provides}
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.context.session.SessionStore
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.{PlayCookieSessionStore, ShiroAesDataEncrypter}
import org.pac4j.play.{CallbackController, LogoutController}
import play.api.{Configuration, Environment}
import security.SecurityModule._

class SecurityModule(environment: Environment, config: Configuration) extends AbstractModule {

  override def configure(): Unit = {

    val playSecretKey = config.get[String]("play.http.secret.key")
    val dataEncrypter = new ShiroAesDataEncrypter(playSecretKey.getBytes)
    val playSessionStore = new PlayCookieSessionStore(dataEncrypter)

    bind(classOf[SessionStore]).toInstance(playSessionStore)
    bind(classOf[SecurityComponents]).to(classOf[DefaultSecurityComponents])

    val callbackController = new CallbackController()
    callbackController.setDefaultUrl("/signin/success")
    callbackController.setRenewSession(false)
    bind(classOf[CallbackController]).toInstance(callbackController)

    val logoutController = new LogoutController()
    logoutController.setDefaultUrl("/")
    logoutController.setDestroySession(true)
    bind(classOf[LogoutController]).toInstance(logoutController)
  }

  @Provides
  def provideFormClient(authenticator: FormAuthenticator): FormClient =
    new FormClient(LoginUrl, UsernameParameter, PasswordParameter, authenticator) {
      setName(ClientName)

      override protected def computeErrorMessage(e: Exception): String = e.getMessage
    }

  @Provides
  def provideConfig(formClient: FormClient, sessionStore: SessionStore): Config = {
    val baseUrl = this.config.get[String]("base-url")
    val clients = new Clients(getCallbackUrl(baseUrl), formClient)
    val config = new Config(clients)
    config.setSessionStore(sessionStore)
    config
  }
}

object SecurityModule {
  private val UsernameParameter: String = "email"
  private val PasswordParameter: String = "password"
  private val LoginUrl: String = "/signin"
  private val ClientName: String = "FormClient"

  private def getCallbackUrl(baseUrl: String) = s"$baseUrl/callback"
}
