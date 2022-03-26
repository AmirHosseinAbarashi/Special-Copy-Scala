object Main {
  def main(args: Array[String]): Unit = {

    val copyDirectory = new CopyDirectory
    copyDirectory.copy(scala.io.StdIn.readLine(), scala.io.StdIn.readLine())

  }
}
