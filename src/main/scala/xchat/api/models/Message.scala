package xchat.api.models

/** A message to be passed to groups
  * @constructor creates a new message with the given text
  * @param id the identification number
  * @param text the message text
  */
case class Message( id: Long, text: String )