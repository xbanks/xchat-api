package xchat.api.repositories

import xchat.api.models.generated.Tables

import scala.concurrent.Future

/** Holds a list of Messages
  * Used in order to keep track of messages for a given group
  */
class MessageRepo extends Repository( new Tables.Messages(_) ) {
  import profile.api._

  /** Find a message by the message id
    *
    * @param id
    * @return
    */
  def findById(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.id === id).result)

  /** Find a list of messages sent from a given user
    *
    * @param id
    * @return
    */
  def findBySender(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.senderId === id).result)

  /** find the messages sent to a given group
    *
    * @param id
    * @return
    */
  def findByGroup(id: Int): Future[Seq[Tables.MessagesRow]] = db.run(table.filter(_.groupId === id).result)

  /** Insert a message into the database, sending a message to a given group
    *
    * @param row
    * @return
    */
  override def insert(row: Tables.MessagesRow): Future[Int] = db.run(table += row)

  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)
}
