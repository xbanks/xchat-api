package xchat.api.util

import com.typesafe.config.ConfigFactory
/**
  * Created by xavier on 6/4/16.
  */
trait DatabaseConfig {
  val driver = slick.driver.SQLiteDriver
  import driver.api._

  val config        = Config.get("db-config.default-db")
  val slickDriver   = Config.get(s"$config.slick-driver")
  val jdbcDriver    = Config.get(s"$config.driver")
  val url           = Config.get(s"$config.url")
  val pkg           = Config.get("slick-codegen.pkg")
  val outputFolder  = Config.get("slick-codegen.output-folder")

  def db = Database.forConfig(config)

  implicit val session: Session = db.createSession()
}

object Config {
  def get(value: String) = ConfigFactory.load().getString(value)
}