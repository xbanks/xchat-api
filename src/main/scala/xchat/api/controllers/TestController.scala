package xchat.api.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.{Future => TFuture}

/**
  * Created by xavier on 6/26/16.
  */
class TestController extends Controller {

  get( "/test" ) { request: Request =>
    TFuture {
      Thread.sleep(3000)
      1
    }
  }

}
