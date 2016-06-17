package xchat.api.services

import xchat.api.models.generated.Tables.MessagesRow
import xchat.api.repositories.MessageRepo
import xchat.api.services.MessageService._


/** The Akka service used to interact with a given message repository
  * TODO : Make sure to only delete messages that were sent by that user...
  */
class MessageService extends MessageRepo with Service{
  val serviceName = "MessageService"

  override def receive: Receive = {
    case Send(msg) => insert(msg) onComplete handleIR(frozenSender = sender)
    case FindMessagesFromGroup(id) => findByGroup(id) onComplete handleFindSeq(s"FindMessagesFromGroup: $id")
  }

}

object MessageService {
  case class Send(message: MessagesRow)
  case class FindMessagesFromGroup(id: Int)
}