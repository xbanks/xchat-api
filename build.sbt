import sbt.Keys._

name := "xchat-api"

version := "1.0"

lazy val mainProject = Project(
  id="xchat-api",
  base = file("."),
  settings = Defaults.coreDefaultSettings ++ Seq(
    scalaVersion := "2.11.8",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "org.scalatest"         %   "scalatest_2.11"      % "2.2.6" % "test",
      "com.typesafe.akka"     %%  "akka-actor"          % "2.4.6",
      "com.typesafe.akka"     %%  "akka-testkit"        % "2.4.6" % "test",
      "com.typesafe.play"     %%  "anorm"               % "2.5.0",
      "com.typesafe.slick"    %%  "slick"               % "3.1.1",
      "com.typesafe.slick"    %%  "slick-codegen"       % "3.1.1",
      "org.slf4j"             %   "slf4j-nop"           % "1.6.4",
      "org.xerial"            %   "sqlite-jdbc"         % "3.7.2",
      "com.twitter.finatra"   %%  "finatra-http"        % "2.1.6",
      "com.twitter.finatra"   %%  "finatra-jackson"     % "2.1.6",
      "com.twitter"           %%  "bijection-core"      % "0.9.2",
      "com.twitter"           %%  "bijection-util"      % "0.9.2",
      "com.github.t3hnar"     %% "scala-bcrypt"         % "2.6"

    ),

    slick <<= slickCodeGenTask//,
//    sourceGenerators in Compile <+= slickCodeGenTask
  )
)


lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (scalaSource in Compile, dependencyClasspath in Compile, runner in Compile, streams, resourceDirectory in Compile) map { (dir, cp, r, s, res) =>
  val outputDir = dir.getPath
  val resDir = res.getPath
  val url = s"jdbc:sqlite:$resDir/chatDB"
  val jdbcDriver = "org.sqlite.JDBC"
  val slickDriver = "slick.driver.SQLiteDriver"
  val pkg = "xchat.api.models.generated"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
//  toError(r.run("xchat.api.util.CustomSourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  val filename = outputDir + "/xchat/api/models/generated/Tables.scala"
  Seq(file(filename))
}