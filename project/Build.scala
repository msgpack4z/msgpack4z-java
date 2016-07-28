object build {

  def gitHash: String = scala.util.Try(
    sys.process.Process("git rev-parse HEAD").lines_!.head
  ).getOrElse("master")

  val msgpack4zJavaName = "msgpack4z-java"

  val modules = msgpack4zJavaName :: Nil

}
