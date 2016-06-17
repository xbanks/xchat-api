package xchat.api.services

import xchat.api.models.generated.Tables.GroupsRow
import xchat.api.repositories.GroupRepo
import xchat.api.services.GroupService._

/** This Actor represents all of the actions that can be
  * completed with a group
  *
  * Actions:
  *   Inserting a new group
  *
  */
class GroupService extends GroupRepo with Service {
  val serviceName = "GroupService"

  override def receive: Receive = {
    case str: String => log.info(s"Test from groupService: $str")
    case Insert(row)    => insert(row) onComplete handleIR()
    case FindAll => findAll() onComplete handleFindSeq("FindAll")
    case FindById(id) => findById(id) onComplete handleFindOne(s"FindById: $id")
    case Remove(id) => handle(s"Remove: $id")(remove(id))
  }
}

object GroupService {
  sealed trait ServiceRequest
  case class  Insert(row: GroupsRow) extends ServiceRequest
  case object FindAll extends ServiceRequest
  case class  FindById(id: Int) extends ServiceRequest
  case class  Remove(id: Int)
}
