package xchat.api.repositories

import xchat.api.models.generated.Tables
import xchat.api.models.generated.Tables.UserGroupRow

import scala.concurrent.Future

/**
  * Created by xavier on 6/8/16.
  */
class UserGroupRepo extends Repository( new Tables.UserGroup(_) ){
  import profile.api._
  override def insert(row: Tables.UserGroupRow): Future[Int] = db.run(table += row)
  def findByUserId(id: Int): Future[Seq[Tables.UserGroupRow]] = db.run(table.filter(_.userId === id).result)
  def findByGroupId(id: Int): Future[Seq[Tables.UserGroupRow]] = db.run(table.filter(_.groupId === id).result)
  def findByUserGroup(userGroupRow: UserGroupRow): Future[Option[Tables.UserGroupRow]] = {
    db.run(table.filter { row =>
      row.userId  === userGroupRow.userId &&
      row.groupId === userGroupRow.groupId
    }.result.headOption)
  }

  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)
  def remove(userGroupRow: UserGroupRow): Future[Int] = {
    db.run(table.filter{ row =>
      row.userId === userGroupRow.userId &&
      row.groupId === userGroupRow.groupId
    }.delete)
  }

}
