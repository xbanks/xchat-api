package xchat.api.repositories

import xchat.api.models.generated._

import scala.concurrent.Future

/** Holds a list of Groups
  * Used in order to keep track of groups and interact with the list of them
  */
class GroupRepo extends Repository(tag => new Tables.Groups(tag)) {
  import profile.api._
  type SeqFuture = Future[Seq[Tables.GroupsRow]]

  /** Creating a new group
    *
    * @param row
    * @return
    */
  def insert(row: Tables.GroupsRow): Future[Int] = {
    db.run(table += row)
  }

  /** Removing a group
    *
    * @param id
    * @return
    */
  def remove(id: Int): Future[Int] = {
    db.run(table.filter(_.id === id).delete)
  }

  /** Finding a group by the group's id
    *
    * @param id
    * @return
    */
  def findById(id: Int): Future[Option[Tables.GroupsRow]] = {
    db.run(table.filter(_.id === id).result.headOption)
  }

  /** Finding the group by it's name
    *
    * @param name
    * @return
    */
  def findByName(name: String): Future[Seq[Tables.GroupsRow]] = {
    db.run(table.filter(_.name === name).result)
  }

  /** Finding all groups administrated by the user with the given id
    *
    * @param id
    * @return
    */
  def findByAdministrator(id: Int): Future[Seq[Tables.GroupsRow]] = {
    db.run(table.filter(_.adminId === id).result)
  }

  /** Find all groups with a given privacy
    * @todo Probably change privacy to privacy level to account for different types of privacy
    * @param isPrivate
    * @return
    */
  def findByPrivacy(isPrivate: Boolean = true): SeqFuture = {
    db.run(table.filter(_.`private` === (if(isPrivate) 1 else 0)).result)
  }
}
