package xchat.api.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import xchat.api.repositories.MessageRepo

/**
  * Created by xavier on 6/18/16.
  */
object ServiceModule extends TwitterModule {

  @Singleton
  @Provides
  def messageServiceProvider: MessageRepo = {
    new MessageRepo()
  }

}
