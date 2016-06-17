package xchat.api.services

import xchat.api.models.generated.Tables.UserGroupRow
import xchat.api.repositories.UserGroupRepo
import xchat.api.services.UserGroupService._


/**
  * Created by xavier on 6/8/16.
  */
class UserGroupService extends UserGroupRepo with Service  {
  val serviceName = "UserGroupService"

  override def receive: Receive = {
    case Insert(userGroupRow) => handle("Insert")(insert(userGroupRow))
    case GetByUserId(id) => findByUserId(id) onComplete handleFindSeq(s"GetByUserId: $id")
    case GetByGroupId(id) => findByGroupId(id) onComplete handleFindSeq(s"GetByGroupId: $id")
    case Remove(id)   => remove(id) onComplete handleIR(s"Remove: $id", sender)
  }
}

object UserGroupService {
  case class Insert(row: UserGroupRow)
  case class GetByUserId(id: Int)
  case class GetByGroupId(id: Int)
  case class Remove(id: Int)
}