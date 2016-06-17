package xchat.api.services

import akka.actor.{Actor, ActorRef}
import akka.event.{Logging, LoggingAdapter}
import xchat.api.repositories.Repository

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/** The general Service skeleton.
  * This is an Akka Actor used to allow interaction between the application and a given repository.
  * Because of this, all members of this class are required to inherit from a Repository class
  */
trait Service extends Actor {
  this: Repository[_] =>
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  implicit val log: LoggingAdapter = Logging(context.system, this)
  val serviceName: String
  import ServiceResponses._


  /** Handles the values returned by a future returning a possible Sequence Value
    *
    * @param action The name of the action being performed, eg. "Insert"/"Remove"/"FindById", etc...
    * @param frozenSender A frozen copy of the reference to the message
    * @param complete The value provided by a Futures onComplete function
    * @param log A log that will be used to for logging results
    */
  def handleFindSeq(action: String, frozenSender: ActorRef = sender)
                   (complete: Try[Seq[_]])
                   (implicit log: LoggingAdapter) = complete match {
    case Success(rows: Seq[_])  =>
      log.debug(s"[$serviceName][$action]: Rows Found \n\t${rows.mkString("\n\t")}")
      frozenSender ! (if(rows.isEmpty) Empty else SeqResponse(rows))
    case Failure(exception)     =>
      log.error(s"[$serviceName][$action]: Failure: ${exception.getMessage}")
      frozenSender ! ServiceFailure
  }

  /** Handles the values returned by a future returning a possible Int Value, mostly used by Insert and Remove functions
    *
    * @param action The name of the action being performed, eg. "Insert"/"Remove"
    * @param frozenSender A frozen copy of the reference to the message
    * @param complete The value provided by a Futures onComplete function
    * @param log A log that will be used for logging results
    */
  def handleIR(action: String = "Insert", frozenSender: ActorRef = sender)
              (complete: Try[Int])
              (implicit log: LoggingAdapter) = complete match {
    case Success(rowsAffected: Int)   =>
      log.debug(s"[$serviceName][$action]: Rows Affected: $rowsAffected")
      frozenSender ! IRResponse(rowsAffected)
    case Failure(exception)           =>
      log.error(s"[$serviceName][$action]: Failure: ${exception.getMessage}")
      frozenSender ! ServiceFailure
  }

  /** Handles the values returned by a future returning a possible Single DB Value
    *
    * @param action The name of the action being performed, eg. "FindById"/"FindByGroup", etc...
    * @param frozenSender A frozen copy of the reference to the message sender
    * @param complete The value provided by a Futures onComplete function
    * @param log A log that will be used for logging results
    */
  def handleFindOne[T](action: String, frozenSender: ActorRef = sender)
                      (complete: Try[T])
                      (implicit log: LoggingAdapter) = complete match {
    case Success(rowSeq: Seq[_])        =>
      val row = if(rowSeq.isEmpty) Empty else rowSeq.head
      log.debug(s"[$serviceName][$action]: Row Found: $row")
      frozenSender ! ( if(row == Empty) row else RowResponse(row) )
    case Failure(exception)     =>
      log.error(s"[$serviceName][$action]: Failure: ${exception.getMessage}")
      frozenSender ! ServiceFailure
  }

  /** This handles the Futures and maps them to the corresponding handler function
 *
    * @note DOES NOT CURRENTLY WORK RIGHT, USE HANDLER FUNCTIONS DIRECTLY
    * @param action The name of the action being performed, eg. "Insert"/"FindById", etc...
    * @param future The future being handled
    * @param log A log that will be used for logging results
    */
  def handle(action: String, frozenSender: ActorRef = sender)(future: Future[_])(implicit log: LoggingAdapter) = future match {
    case f: Future[Int]     => f onComplete handleIR(action, frozenSender)
    case f: Future[Seq[_]]  => f onComplete handleFindSeq(action, frozenSender)
    case f: Future[_]       => f onComplete handleFindOne(action, frozenSender)
    case _ => log.warning("Could not figure out type?")
  }
}

object ServiceResponses {
  sealed trait ServiceResponse

  sealed trait  ServiceSuccess                    extends ServiceResponse
  case object   ServiceSuccess                    extends ServiceSuccess
  case class    Empty(msg: String = "Not Found")  extends ServiceSuccess
  case class    IRResponse(rowsAffected: Int)     extends ServiceSuccess
  case class    RowResponse[T](row: T)            extends ServiceSuccess {
    require(!row.isInstanceOf[Iterable[_]], "RowResponse should not hold Iterable types. Use ServiceResponse.SeqResponse instead.")
  }
  case class    SeqResponse[T](rows: Seq[T])      extends ServiceSuccess {
    require(rows.nonEmpty, "SeqResponse cannot be empty, ServiceResponse.Empty should be used.")
  }

  sealed trait ServiceFailure extends ServiceResponse
  case object ServiceFailure extends ServiceFailure
}