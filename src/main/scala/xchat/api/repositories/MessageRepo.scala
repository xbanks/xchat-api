package xchat.api.repositories

import xchat.api.models.generated.Tables

import scala.concurrent.Future

/** Holds a list of Messages
  * Used in order to keep track of messages for a given group
  */
class MessageRepo extends Repository( new Tables.Messages(_) ) {
  import profile.api._

  def findById(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.id === id).result)

  def findBySender(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.senderId === id).result)

  def findByGroup(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.groupId === id).result)

  override def insert(row: Tables.MessagesRow): Future[Int] = db.run(table += row)

  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)
}
