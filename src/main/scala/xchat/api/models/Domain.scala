package xchat.api.models

import com.twitter.finatra.request.QueryParam
import xchat.api.models.generated.Tables._

/**
  * Created by xavier on 6/18/16.
  */
object Domain {
  sealed trait DomainObject

  implicit def messageMapper(message: Message): MessagesRow = {
    MessagesRow.tupled( Message.unapply(message).get )
  }

  implicit def groupMapper(group: Group): GroupsRow = {
    GroupsRow.tupled( Group.unapply(group).get )
  }

  implicit def userGroupMapper(userGroup: UserGroup): UserGroupRow = {
    UserGroupRow tupled UserGroup.unapply(userGroup).get
  }

  implicit def userMapper(user: User): UsersRow = {
    UsersRow.tupled(User.unapply(user).get)
  }

  implicit def toSafeUser(user: UsersRow): SafeUser = {
    SafeUser(user.id, user.name)
  }

  case class Message(id: Option[Int], senderId: Option[Int], groupId: Int, content: String, time: Option[String]) extends DomainObject
  case class Group(id: Option[Int], name: String, adminId: Option[Int], `private`: Option[Int]) extends DomainObject
  case class UserGroup(id: Option[Int], userId: Int, groupId: Int) extends DomainObject
  case class User(id: Option[Int], name: String, passwd: String) extends DomainObject
  case class SafeUser(id: Option[Int], name: String) extends DomainObject
  case class GroupRequest(
                         @QueryParam name: Option[String] = None,
                         @QueryParam id: Option[Int] = None
                         )
}