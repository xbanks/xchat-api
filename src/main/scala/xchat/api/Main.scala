package xchat.api

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import xchat.api.controllers._
import xchat.api.util.DatabaseConfig

/**
  * Created by xavier on 6/17/16.
  */
object Main extends DatabaseConfig with HttpServer {
  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[CommonFilters]
      .add[TestController]
      .add[MessageController]
      .add[UserController]
      .add[GroupController]
      .add[UserGroupController]
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
