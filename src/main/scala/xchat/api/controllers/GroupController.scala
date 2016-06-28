package xchat.api.controllers

import com.google.inject.Inject
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.{Future => TwitterFuture}
import xchat.api.models.Domain.Group
import xchat.api.models.generated.Tables.GroupsRow
import xchat.api.repositories.GroupRepo

/** The HTTP Controller for all of the actions related to the [[xchat.api.repositories.GroupRepo]]
  * This controller handles the creation, deletion and finding of [[GroupsRow]]s
  * This controller does not handle the addition or removal of users to and from groups, that is handled by the [[UserGroupController]]
  * This controller also does not handle the message passing withing a group, that is handled in the [[MessageController]]
  */
class GroupController @Inject() (groupRepo: GroupRepo) extends Controller {
  implicit val ec = scala.concurrent.ExecutionContext.global

  get( route = "/groups", name = "Get all groups" ) { request: Request =>
    val groups = groupRepo.findAll().as[TwitterFuture[Seq[GroupsRow]]]

    groups.handle { case _ => response.internalServerError("Something went wrong, could not find groups") }
  }

  post( route = "/groups", name = "Add a new group" ) { domainGroup: Group =>
    val affectedRows = groupRepo.insert(domainGroup).as[TwitterFuture[Int]]

    affectedRows
        .map {
          case 0 => response.badRequest("Group could not be created")
          case 1 => response.ok("Group creation successful")
          case _ => response.badRequest("How did you manage to affect more than one rows????")
        }
        .handle {
          case _ => response.internalServerError("Something went wrong, could not create the group")
        }
  }

  get( route = "/groups/:id", name = "Get a group by id" ) { request: Request =>
    val id = request.getIntParam("id")
    val group = groupRepo.findById(id).as[TwitterFuture[Option[GroupsRow]]]


    group.map{
      case Some(g) => g
      case None => response.ok("Group DNE")
    }.handle{ case _ => response.internalServerError("Something went wrong Could not find the group") }
  }
}
