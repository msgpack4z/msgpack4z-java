import sbtrelease._
import ReleaseStateTransformations._
import build._

ReleasePlugin.extraReleaseCommands

autoScalaLibrary := false

crossPaths := false

name := msgpack4zJavaName

compile / javacOptions ++= Seq("-target", "8", "-source", "8")

commands += Command.command("updateReadme")(UpdateReadme.updateReadmeTask)

libraryDependencies ++= (
  ("org.msgpack" % "msgpack-core" % "0.9.12") ::
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
  releaseStepCommand("sonaRelease"),
  pushChanges
)

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

publishTo := (if (isSnapshot.value) None else localStaging.value)
