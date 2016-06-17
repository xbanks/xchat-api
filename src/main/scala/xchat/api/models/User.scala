package xchat.api.models

/** A user that participates in group chat
  * TODO: add some kind of authentication mechanism
  * @constructor creates a new User
  * @param id the identification number
  * @param name the user's name
  */
case class User( id: Long, name: String )
