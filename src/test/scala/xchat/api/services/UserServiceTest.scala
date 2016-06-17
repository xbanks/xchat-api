package xchat.api.services

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import xchat.api.models.generated.Tables.UsersRow
import xchat.api.services.ServiceResponses._
import xchat.api.services.UserService._

import scala.concurrent.duration._

/**
  * Created by xavier on 6/12/16.
  */
class UserServiceTest extends TestKit(ActorSystem("user-service-test-system"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A UserService" should {
    val userServiceTest = system.actorOf(Props[UserService], "user-service-test-actor")
    val testUserId = 999
    val testUser = UsersRow(Some(testUserId), "Test Guy", "TestPassword")

    "create a user within 2 seconds" in {
      within( 2 seconds ) {
        userServiceTest ! Create( testUser )
        expectMsg(IRResponse(1))
      }
    }

    "find a user by id within 2 seconds" in {
      within( 2 seconds ) {
        userServiceTest ! FindUserById(testUserId)
        expectMsg(RowResponse(testUser))
      }
    }

    "find users by name within 2 seconds" in {
      within( 2 seconds ) {
        userServiceTest ! FindUserByName(testUser.name)
        expectMsg(SeqResponse(Seq(testUser)))
      }
    }

    "delete a user within 2 seconds" in {
      within( 2 seconds ) {
        userServiceTest ! Remove(testUserId)
        expectMsg(IRResponse(1))
      }
    }

    "fail when attempting to find a nonexistent user" in {
      within( 2 seconds ) {
        userServiceTest ! FindUserById(testUserId)
        expectMsg(Empty)
      }
    }
  }
}

