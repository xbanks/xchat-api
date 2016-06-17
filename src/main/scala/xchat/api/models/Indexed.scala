package xchat.api.models

/** A model is indexed so that it may be recorded in a repository,
  * therefore it needs an id.
  */
trait Indexed {
  val id: Option[Int]
}
