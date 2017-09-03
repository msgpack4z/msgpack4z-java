import sbtrelease._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys
import build._

ReleasePlugin.extraReleaseCommands

resolvers += Opts.resolver.sonatypeReleases

autoScalaLibrary := false

crossPaths := false

name := msgpack4zJavaName

javacOptions in compile ++= Seq("-target", "6", "-source", "6")

javacOptions in (Compile, doc) ++= Seq("-locale", "en_US")

commands += Command.command("updateReadme")(UpdateReadme.updateReadmeTask)

libraryDependencies ++= (
  ("org.msgpack" % "msgpack-core" % "0.8.13") ::
  ("com.github.xuwei-k" % "msgpack4z-api" % "0.2.0") ::
  Nil
)

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
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

credentials ++= PartialFunction.condOpt(sys.env.get("SONATYPE_USER") -> sys.env.get("SONATYPE_PASS")){
  case (Some(user), Some(pass)) =>
    Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass)
}.toList

organization := "com.github.xuwei-k"

homepage := Some(url("https://github.com/msgpack4z"))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  Nil
)

crossScalaVersions := scalaVersion.value :: Nil

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


description := "msgpack4z"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
