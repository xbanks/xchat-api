package xchat.api.services

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.joda.time.LocalDateTime
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import xchat.api.models.generated.Tables.MessagesRow
import xchat.api.services.MessageService._
import xchat.api.services.ServiceResponses._

import scala.concurrent.duration._

/**
  * Created by xavier on 6/12/16.
  */
class MessageServiceTest extends TestKit(ActorSystem("message-service-test-system"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A MessageService" should {
    val messageServiceTest = system.actorOf(Props[MessageService], "msg-service-test")
    "send a message within 2 second" in {
      within( 1.5 seconds ) {
        val message = MessagesRow(None, Some(1), 3, "Hey Everyone, this is from a test", time = Some(LocalDateTime.now().toString) )
        messageServiceTest ! Send( message )
        expectMsg( IRResponse(1) )
      }
    }

    "find messages from a group within 2 seconds" in {
      within( 1.5 seconds ) {
        messageServiceTest ! FindMessagesFromGroup(3)
        expectMsgPF() {
          case SeqResponse(_) => true
          case _ => false
        }
      }
    }
  }

}
