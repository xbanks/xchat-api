package xchat.api.models.generated
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.SQLiteDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Groups.schema ++ Messages.schema ++ UserGroup.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Groups
   *  @param id Database column id SqlType(INTEGER), PrimaryKey
   *  @param name Database column name SqlType(TEXT)
   *  @param adminId Database column admin_id SqlType(INTEGER)
   *  @param `private` Database column private SqlType(INTEGER) */
  case class GroupsRow(id: Option[Int], name: String, adminId: Option[Int], `private`: Option[Int])
  /** GetResult implicit for fetching GroupsRow objects using plain SQL queries */
  implicit def GetResultGroupsRow(implicit e0: GR[Option[Int]], e1: GR[String]): GR[GroupsRow] = GR{
    prs => import prs._
    GroupsRow.tupled((<<?[Int], <<[String], <<?[Int], <<?[Int]))
  }
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: private */
  class Groups(_tableTag: Tag) extends Table[GroupsRow](_tableTag, "groups") {
    def * = (id, name, adminId, `private`) <> (GroupsRow.tupled, GroupsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id, Rep.Some(name), adminId, `private`).shaped.<>({r=>import r._; _2.map(_=> GroupsRow.tupled((_1, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey)
    /** Database column name SqlType(TEXT) */
    val name: Rep[String] = column[String]("name")
    /** Database column admin_id SqlType(INTEGER) */
    val adminId: Rep[Option[Int]] = column[Option[Int]]("admin_id")
    /** Database column private SqlType(INTEGER)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `private`: Rep[Option[Int]] = column[Option[Int]]("private")

    /** Foreign key referencing Users (database name users_FK_1) */
    lazy val usersFk = foreignKey("users_FK_1", adminId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.SetDefault)
  }
  /** Collection-like TableQuery object for table Groups */
  lazy val Groups = new TableQuery(tag => new Groups(tag))

  /** Entity class storing rows of table Messages
   *  @param id Database column id SqlType(INTEGER), PrimaryKey
   *  @param senderId Database column sender_id SqlType(INTEGER)
   *  @param groupId Database column group_id SqlType(INTEGER)
   *  @param content Database column content SqlType(TEXT)
   *  @param time Database column time SqlType(DATETIME) */
  case class MessagesRow(id: Option[Int], senderId: Option[Int], groupId: Int, content: String, time: Option[String])
  /** GetResult implicit for fetching MessagesRow objects using plain SQL queries */
  implicit def GetResultMessagesRow(implicit e0: GR[Option[Int]], e1: GR[Int], e2: GR[String], e3: GR[Option[String]]): GR[MessagesRow] = GR{
    prs => import prs._
    MessagesRow.tupled((<<?[Int], <<?[Int], <<[Int], <<[String], <<?[String]))
  }
  /** Table description of table messages. Objects of this class serve as prototypes for rows in queries. */
  class Messages(_tableTag: Tag) extends Table[MessagesRow](_tableTag, "messages") {
    def * = (id, senderId, groupId, content, time) <> (MessagesRow.tupled, MessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id, senderId, Rep.Some(groupId), Rep.Some(content), time).shaped.<>({r=>import r._; _3.map(_=> MessagesRow.tupled((_1, _2, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey)
    /** Database column sender_id SqlType(INTEGER) */
    val senderId: Rep[Option[Int]] = column[Option[Int]]("sender_id")
    /** Database column group_id SqlType(INTEGER) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column content SqlType(TEXT) */
    val content: Rep[String] = column[String]("content")
    /** Database column time SqlType(DATETIME) */
    val time: Rep[Option[String]] = column[Option[String]]("time")

    /** Foreign key referencing Groups (database name groups_FK_1) */
    lazy val groupsFk = foreignKey("groups_FK_1", Rep.Some(groupId), Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name users_FK_2) */
    lazy val usersFk = foreignKey("users_FK_2", senderId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.SetDefault)
  }
  /** Collection-like TableQuery object for table Messages */
  lazy val Messages = new TableQuery(tag => new Messages(tag))

  /** Entity class storing rows of table UserGroup
   *  @param id Database column id SqlType(INTEGER), PrimaryKey
   *  @param userId Database column user_id SqlType(INTEGER)
   *  @param groupId Database column group_id SqlType(INTEGER) */
  case class UserGroupRow(id: Option[Int], userId: Int, groupId: Int)
  /** GetResult implicit for fetching UserGroupRow objects using plain SQL queries */
  implicit def GetResultUserGroupRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[UserGroupRow] = GR{
    prs => import prs._
    UserGroupRow.tupled((<<?[Int], <<[Int], <<[Int]))
  }
  /** Table description of table user_group. Objects of this class serve as prototypes for rows in queries. */
  class UserGroup(_tableTag: Tag) extends Table[UserGroupRow](_tableTag, "user_group") {
    def * = (id, userId, groupId) <> (UserGroupRow.tupled, UserGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id, Rep.Some(userId), Rep.Some(groupId)).shaped.<>({r=>import r._; _2.map(_=> UserGroupRow.tupled((_1, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey)
    /** Database column user_id SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column group_id SqlType(INTEGER) */
    val groupId: Rep[Int] = column[Int]("group_id")

    /** Foreign key referencing Groups (database name groups_FK_1) */
    lazy val groupsFk = foreignKey("groups_FK_1", Rep.Some(groupId), Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name users_FK_2) */
    lazy val usersFk = foreignKey("users_FK_2", Rep.Some(userId), Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)

    /** Uniqueness Index over (userId,groupId) (database name sqlite_autoindex_user_group_1) */
    val index1 = index("sqlite_autoindex_user_group_1", (userId, groupId), unique=true)
  }
  /** Collection-like TableQuery object for table UserGroup */
  lazy val UserGroup = new TableQuery(tag => new UserGroup(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(INTEGER), PrimaryKey
   *  @param name Database column name SqlType(TEXT)
   *  @param passwd Database column passwd SqlType(TEXT) */
  case class UsersRow(id: Option[Int], name: String, passwd: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Option[Int]], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<?[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[UsersRow](_tableTag, "users") {
    def * = (id, name, passwd) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id, Rep.Some(name), Rep.Some(passwd)).shaped.<>({r=>import r._; _2.map(_=> UsersRow.tupled((_1, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey)
    /** Database column name SqlType(TEXT) */
    val name: Rep[String] = column[String]("name")
    /** Database column passwd SqlType(TEXT) */
    val passwd: Rep[String] = column[String]("passwd")
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
