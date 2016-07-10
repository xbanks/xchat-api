package xchat.api.controllers

import com.google.inject.Inject
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.{Future => TwitterFuture}
import xchat.api.models.Domain._
import xchat.api.models.generated.Tables.UsersRow
import xchat.api.repositories.UserRepo

/**
  * Created by xavier on 6/25/16.
  */
class UserController @Inject() (userRepo: UserRepo) extends Controller {
  implicit val ec = scala.concurrent.ExecutionContext.global

  get( route = "/users", name = "Get All Users") { request: Request =>
    val allUsers = userRepo.findAll().as[TwitterFuture[Seq[UsersRow]]]
    allUsers.map{ users =>
      users.map{ user => SafeUser(user.id, user.name) }
    }
  }

  /** Finding a user by their id
    * None of these return the full users information,
    * they are returned in a SafeUser object, removing the password.
    */
  get( route = "/users/:id", name = "Get User By Id" ) { request: Request =>
    val id = request.getIntParam("id")
    val user = userRepo.findById(id).as[TwitterFuture[Option[UsersRow]]]

    user.map{ u => SafeUser(u.get.id, u.get.name) }
        .handle { case _ => response.badRequest("User does not exist") }
  }

  /** Adding a new user to the database
    * Right now this only returns the amount of rows affected by the insertion.
    * Once I get the SQLite stuff working correctly, it will return the new users id.
    */
  post( route = "/users", name = "Create a new User" ) { user: User =>
    val usersRow: UsersRow = user.copy(id = None)
    val affectedRows = userRepo.insert(usersRow).as[TwitterFuture[Int]]

    affectedRows
      .map {
        case 0 => response.badRequest("User creation failed")
        case 1 => response.ok("User creation successful")
        case _ => response.ok("Somehow you affected more than one row...")
      }
      .handle {
        case _ => response.internalServerError("Something went wrong")
      }
  }

}
