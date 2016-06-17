package xchat.api.repositories

import xchat.api.models.generated.Tables

import scala.concurrent.Future

/**
  * Created by xavier on 6/8/16.
  */
class UserRepo extends Repository( new Tables.Users(_) ){
  import profile.api._

  // TODO: Add code for retrieving ID after insert once the schema generation generates AutoInc keys
  override def insert(row: Tables.UsersRow): Future[Int] = db.run(table += row)

  def findById(id: Int) = db.run(table.filter(_.id === id).result)
  def findByName(name: String) = db.run(table.filter(_.name === name).result)

  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)
}
