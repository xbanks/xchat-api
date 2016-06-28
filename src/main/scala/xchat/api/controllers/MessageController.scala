package xchat.api.controllers

import java.time.LocalDateTime
import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.util.{Await, Future => TwitterFuture}
import xchat.api.models.Domain
import xchat.api.models.generated.Tables.MessagesRow
import xchat.api.models.generated._
import xchat.api.repositories.MessageRepo

import scala.concurrent.{Future, Await => SAwait}
import scala.concurrent.duration._

/**
  * Created by xavier on 6/18/16.
  */
class MessageController @Inject()(messageRepo: MessageRepo) extends Controller {
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
//  implicit def toTwitterFuture[T](scalaFuture: Future[T]): TwitterFuture[T] = {
//    scalaFuture.as[TwitterFuture[T]]
//  }

  get( "/groups/:id/messages" ) { request: Request =>
    val groupId = request.getIntParam("id")
    val twitterFuture = messageRepo.findByGroup(groupId).as[TwitterFuture[Seq[MessagesRow]]]

    twitterFuture
  }

  post( "/messages" ) { request: Domain.Message =>
    val row: MessagesRow = request
    val f = messageRepo.insert(row.copy(id = None)).as[TwitterFuture[Int]]
    f.handle {
      case message: java.sql.SQLException if message.getMessage.contains("not in") =>
        response.ok.json(s""" "error": "The user is not in the group they are sending the message to" """)
      case message: java.sql.SQLException =>
        response.ok.json(s""" "error_code": "${message.getErrorCode}" """)
      case message =>
        response.ok.json(s""" "error": "wtf man" """)
    }
  }
}