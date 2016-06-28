package xchat.api.repositories

import xchat.api.models.generated.Tables
import com.github.t3hnar.bcrypt._
import scala.concurrent.Future

/** Created by xavier on 6/8/16.
  */
class UserRepo extends Repository( new Tables.Users(_) ){
  import profile.api._


  /** Inserts a new user into the database, using bcrypt to hash the password field.
    * TODO: Add code for retrieving ID after insert once the schema generation generates AutoInc keys
    * @param row The users row to be inserted
    * @return The number of rows affected by the row insertion, if the insertion was successful then 1, otherwise 0
    */
  override def insert(row: Tables.UsersRow): Future[Int] = db.run(table += row.copy(passwd = row.passwd.bcrypt))

  /** Finds a user by their id
    * @param id The user's id that needs to be found
    * @return 0 or 1 users found by the given id
    */
  def findById(id: Int) = db.run(table.filter(_.id === id).result.headOption)

  /** Find a user by their name
    * TODO: possibly add first and last names?
    * @param name
    * @return a list of users with the matching name
    */
  def findByName(name: String) = db.run(table.filter(_.name === name).result)

  /** Remove a user by their id
    * @param id the user's id that is going to be removed
    * @return the number of rows affected by the row deletion
    */
  override def remove(id: Int): Future[Int] = db.run(table.filter(_.id === id).delete)
}
