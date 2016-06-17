package xchat.api.services

import xchat.api.models.generated.Tables.UsersRow
import xchat.api.repositories.UserRepo
import xchat.api.services.UserService._


/**
  * Created by xavier on 6/8/16.
  */
class UserService extends UserRepo with Service {
  val serviceName = "UserService"

  override def receive: Receive = {
    case Create(user) => insert(user) onComplete handleIR("Create User")
    case Remove(id)   => remove(id) onComplete handleIR(s"Remove User: $id")
    case FindUserById(id) => findById(id) onComplete handleFindOne(s"FindUserById: $id")
    case FindUserByName(name) => findByName(name) onComplete handleFindSeq(s"FindUserByName: $name")
  }
}

object UserService {
  case class Create(user: UsersRow)
  case class Remove(id: Int)
  case class FindUserById(id: Int)
  case class FindUserByName(name: String)
}
