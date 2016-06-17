package xchat.api.services

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import xchat.api.models.generated.Tables
import xchat.api.models.generated.Tables.GroupsRow
import xchat.api.services.GroupService.{FindAll, FindById, Insert, Remove}
import xchat.api.services.ServiceResponses._

import scala.concurrent.duration._

/**
  * Created by xavier on 6/11/16.
  */
class GroupServiceTest extends TestKit(ActorSystem("group-service-test-system"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  "A GroupService" should {
    val groupService = system.actorOf(Props[GroupService], "group-service-test-actor")
    val testGroupId = 999
    val testGroup = GroupsRow(Some(testGroupId), "Juan Group Only", None, None)

    "insert a group within 2 seconds" in {
      within( 2 seconds ) {
        groupService ! Insert(testGroup)
        expectMsg(IRResponse(1))
      }
    }

    "find a group by id within 2 seconds" in {
      within(2 seconds ){
        groupService ! FindById(testGroupId)
        expectMsg(RowResponse(testGroup))
      }
    }

    "find all groups within 2 seconds" in {
      within( 2 seconds ){
        groupService ! FindAll
        expectMsgPF() {
          case SeqResponse(res: Tables.GroupsRow) =>
          case _ => false
        }
      }
    }

    "remove a group within 2 seconds" in {
      within( 2 seconds ){
        groupService ! Remove(testGroupId)
        expectMsg(IRResponse(1))
      }
    }
  }

  override protected def afterAll(): Unit = {
    system.terminate()
  }
}
