package xchat.api.repositories

import slick.lifted.{AbstractTable, TableQuery, _}
import xchat.api.models.generated.Tables
import xchat.api.util.DatabaseConfig

import scala.concurrent.Future

/** The general Repository Skeleton
  *
  * @param cons A Slick cons function for creating a the repository's TableQuery
  * @tparam T The Table type being handled
  */
abstract class Repository[T <: AbstractTable[_]](cons: Tag => T) extends DatabaseConfig {
  val table = new TableQuery(cons)
  val profile = Tables.profile
  import profile.api._

  /** The general find all function for all tables, can be overridden for special functionality
    *
    * @return A Future containing a Sequence of table values
    */
  def findAll(): Future[Seq[T#TableElementType]] = db.run(table.result)

  // TODO: Add code for retrieving ID after insert once the schema generation generates AutoInc keys
  def insert(row: T#TableElementType): Future[Int]
  def remove(id: Int): Future[Int]
}

