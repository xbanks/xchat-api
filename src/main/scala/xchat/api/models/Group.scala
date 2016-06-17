package xchat.api.models

import akka.actor.ActorRef

/** A single group
  * TODO : Maybe add a limit on member size.
  * @constructor creates a new group with the given title, members and message service
  * @param id the group identification number
  * @param title the title of the group, possibly changeable by the user
  * @param members the list of members in the group
  * @param messageService the service used by the Group to pass messages
  * @param isPrivate denotes whether or not the group is a private group, meaning there should be at most, 2 users in the members list
  */
case class Group( id: Long, title: String, members: List[User], messageService: ActorRef, isPrivate: Boolean )  {
  if( isPrivate ) {
    require( members.size <= 2, "This is a private group, only 2 users may join." )
  }
}