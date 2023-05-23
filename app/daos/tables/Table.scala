package daos.tables

import slick.jdbc.JdbcProfile

trait Table {

  protected val profile: JdbcProfile
}
