package xchat.api.repositories

import xchat.api.models.generated._

import scala.concurrent.Future

/** Holds a list of Groups
  * Used in order to keep track of groups and interact with the list of them
  */
class GroupRepo extends Repository(tag => new Tables.Groups(tag)) {
  import profile.api._
  type SeqFuture = Future[Seq[Tables.GroupsRow]]

  def insert(row: Tables.GroupsRow): Future[Int] = {
    db.run(table += row)
  }

  def remove(id: Int): Future[Int] = {
    db.run(table.filter(_.id === id).delete)
  }

  def findById(id: Int): Future[Seq[Tables.GroupsRow]] = {
    db.run(table.filter(_.id === id).result)
  }

  def findByName(name: String): Future[Seq[Tables.GroupsRow]] = {
    db.run(table.filter(_.name === name).result)
  }

  def findByAdministrator(id: Int): Future[Seq[Tables.GroupsRow]] = {
    db.run(table.filter(_.adminId === id).result)
  }

  def findByPrivacy(isPrivate: Boolean = true): SeqFuture = {
    db.run(table.filter(_.`private` === (if(isPrivate) 1 else 0)).result)
  }
}
