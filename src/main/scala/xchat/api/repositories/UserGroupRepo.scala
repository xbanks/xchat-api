package xchat.api.repositories

import xchat.api.models.generated.Tables
import xchat.api.models.generated.Tables.UserGroupRow

import scala.concurrent.Future

/**
  * Created by xavier on 6/8/16.
  */
class UserGroupRepo extends Repository( new Tables.UserGroup(_) ){
  import profile.api._

  /** Inserting a new userGroup row into the database, effectivly adding a user into a group
    * @param row the usergroup row to be inserted
    * @return the number of rows affected by the row insertion
    */
  override def insert(row: Tables.UserGroupRow): Future[Int] = db.run(table += row)

  /** Finding a list of usergroups by the user's id, effectivly finding out all of the groups a user is a part of
    * @param id the user's id that we want to filter with
    * @return a list of rows containing the groups that the user is a part of
    */
  def findByUserId(id: Int): Future[Seq[Tables.UserGroupRow]] = db.run(table.filter(_.userId === id).result)

  /** Finding the list of users in a group
    * @param id the group id that we want to filter by
    * @return a list of rows containing the users that the group contains
    */
  def findByGroupId(id: Int): Future[Seq[Tables.UserGroupRow]] = db.run(table.filter(_.groupId === id).result)

  /** Find a single usergroup row, basically checking if a user is in a given group
    * @param uid the user's id
    * @param gid the group's id
    * @return 0 or 1 rows containing information about that usergroup.
    */
  def findByUserGroup(uid: Int, gid: Int): Future[Option[Tables.UserGroupRow]] = {
    db.run(table.filter { row =>
      row.userId  === uid && row.groupId === gid
    }.result.headOption)
  }

  /** Remove a row by the row id
    * @param id the row id to be removed
    * @return the amount of rows affected by the row removal
    */
  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)

  /** Remove a row by the user group, a more specific way of removing a user from a group
    * @param userGroupRow the usergroup to be removed, the id is not checked.
    * @return the number of rows affected by the row removal
    */
  def remove(userGroupRow: UserGroupRow): Future[Int] = {
    db.run(table.filter{ row =>
      row.userId === userGroupRow.userId &&
      row.groupId === userGroupRow.groupId
    }.delete)
  }

}
