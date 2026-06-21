object build {

  def gitHash: String =
    sys.process.Process("git rev-parse HEAD").lazyLines_!.head

  val msgpack4zJavaName = "msgpack4z-java"

  val modules = msgpack4zJavaName :: Nil

}
