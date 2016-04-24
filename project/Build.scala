import sbt._, Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys

object build extends Build {

  private def gitHash: String = scala.util.Try(
    sys.process.Process("git rev-parse HEAD").lines_!.head
  ).getOrElse("master")

  private val msgpack4zJavaName = "msgpack4z-java"

  val modules = msgpack4zJavaName :: Nil

  lazy val msgpack4zJava = Project("msgpack4z-java", file(".")).settings(
    ReleasePlugin.extraReleaseCommands ++ sonatypeSettings: _*
  ).settings(
    resolvers += Opts.resolver.sonatypeReleases,
    fullResolvers ~= {_.filterNot(_.name == "jcenter")},
    autoScalaLibrary := false,
    crossPaths := false,
    name := msgpack4zJavaName,
    javacOptions in compile ++= Seq("-target", "6", "-source", "6"),
    javacOptions in (Compile, doc) ++= Seq("-locale", "en_US"),
    commands += Command.command("updateReadme")(UpdateReadme.updateReadmeTask),
    libraryDependencies ++= (
      ("org.msgpack" % "msgpack-core" % "0.8.6") ::
      ("com.github.xuwei-k" % "msgpack4z-api" % "0.2.0") ::
      Nil
    ),
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      UpdateReadme.updateReadmeProcess,
      tagRelease,
      ReleaseStep(state => Project.extract(state).runTask(PgpKeys.publishSigned, state)._1),
      setNextVersion,
      commitNextVersion,
      UpdateReadme.updateReadmeProcess,
      ReleaseStep(state =>
        Project.extract(state).runTask(SonatypeKeys.sonatypeReleaseAll, state)._1
      ),
      pushChanges
    ),
    credentials ++= PartialFunction.condOpt(sys.env.get("SONATYPE_USER") -> sys.env.get("SONATYPE_PASS")){
      case (Some(user), Some(pass)) =>
        Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass)
    }.toList,
    organization := "com.github.xuwei-k",
    homepage := Some(url("https://github.com/msgpack4z")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    scalacOptions ++= (
      "-deprecation" ::
      "-unchecked" ::
      "-Xlint" ::
      "-language:existentials" ::
      "-language:higherKinds" ::
      "-language:implicitConversions" ::
      Nil
    ),
    scalaVersion := "2.11.8",
    crossScalaVersions := scalaVersion.value :: Nil,
    pomExtra :=
      <developers>
        <developer>
          <id>xuwei-k</id>
          <name>Kenji Yoshida</name>
          <url>https://github.com/xuwei-k</url>
        </developer>
      </developers>
      <scm>
        <url>git@github.com:msgpack4z/msgpack4z-java.git</url>
        <connection>scm:git:git@github.com:msgpack4z/msgpack4z-java.git</connection>
        <tag>{if(isSnapshot.value) gitHash else { "v" + version.value }}</tag>
      </scm>
    ,
    description := "msgpack4z"
  )

}
