package xchat.api

import xchat.api.util.DatabaseConfig

/**
  * Created by xavier on 6/17/16.
  */
object Main extends DatabaseConfig {
  def main(args: Array[String]) {
  }

  def initSchema() = {
    slick.codegen.SourceCodeGenerator.main(
      Array(
        slickDriver,
        jdbcDriver,
        url,
        outputFolder,
        pkg
      )
    )
  }
}
