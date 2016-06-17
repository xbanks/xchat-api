package xchat.api.services

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

/**
  * Created by xavier on 6/12/16.
  */
class UserGroupServiceTest extends TestKit(ActorSystem("user-service-test-system"))
  with WordSpecLike
  with BeforeAndAfterAll
  with DefaultTimeout {

  val userGroupService = system.actorOf(Props[UserGroupService], "user-service-test-actor")

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "UserGroupService" should {
    "insert within 1 second" in {
      within(1.5 seconds) {
//        userGroupService ! Insert(UserGroupRow(None, 1, 3))
        Thread.sleep(1000)
      }
    }

    "remove within 1 second" in {
      within(1.5 seconds) {
//        userGroupService ! Remove(1)
        Thread.sleep(1000)
      }
    }
  }

}
