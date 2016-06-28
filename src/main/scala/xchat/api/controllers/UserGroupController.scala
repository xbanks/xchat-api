package xchat.api.controllers

import com.google.inject.Inject
import com.twitter.finatra.http.Controller
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finagle.http.Request
import com.twitter.util.{Future => TwitterFuture}
import xchat.api.models.Domain._
import xchat.api.models.generated.Tables.{UserGroupRow, UsersRow}
import xchat.api.repositories.{UserGroupRepo, UserRepo}

/**
  * Created by xavier on 6/26/16.
  */
class UserGroupController @Inject() (userGroupRepo: UserGroupRepo, userRepo: UserRepo) extends Controller {
  implicit val ec = scala.concurrent.ExecutionContext.global

  get( route = "/groups/:id/members" ) { request: Request =>
    val id = request.getIntParam("id")
    val userGroups = userGroupRepo.findByGroupId(id).as[TwitterFuture[Seq[UserGroupRow]]]
    type A = TwitterFuture[Seq[SafeUser]]
    type B = TwitterFuture[Option[UsersRow]]
    val joinMap = (users: A, user: B) => users
        .join( user )
        .flatMap {
          case (u, None) => TwitterFuture.value(u)
          case (u, Some(use)) => TwitterFuture.value(u :+ use.as[SafeUser])
        }


    userGroups.flatMap { rows =>
      rows
        .foldLeft(TwitterFuture.value(Seq.empty[SafeUser])) { (users, row) =>
          val user = userRepo.findById(row.userId).as[B]
          joinMap(users, user)
        }
    }
  }

  post( route = "/groups/:gid/members/:uid" ) { request: Request =>
    val uid = request.getIntParam("uid")
    val gid = request.getIntParam("gid")

    val exists = userGroupRepo.findByUserGroup(uid, gid).as[TwitterFuture[Option[UserGroupRow]]]
    exists.flatMap {
      case Some(x)  => TwitterFuture.value(response.badRequest("User is already in this group"))
      case None     =>
        userGroupRepo
          .insert(UserGroupRow(None, uid, gid))
          .as[TwitterFuture[Int]]
          .map{ _=> response.ok("User has been added.") }
          .handle{ case _=> response.internalServerError("Something went wrong, could not insert this user.") }
    }
  }

  delete( route = "/groups/:id/members/:id" ) { _: Request =>
    response.notImplemented
  }
}
