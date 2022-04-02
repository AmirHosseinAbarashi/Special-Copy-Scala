import java.io.File
import java.nio.file.Files
import java.util.concurrent.Executors
import java.util.logging.Logger
import scala.concurrent.{ExecutionContext, Future}


class CopyDirectory {

  private val logger = Logger.getLogger(getClass.getName)

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  def copy(sourceDirectory: String, destinationDirectory: String): Future[Unit] = {
    val tasks =
      for (f <- new File(sourceDirectory).list()) yield {
        Future {
          call(sourceDirectory, destinationDirectory, f)
        }
      }

    Future.sequence(tasks.toSeq).map(_ => ())

  }

  def call(fileIn: String, fileOut: String, f: String): Unit = {
    if (new File(fileIn, f).isDirectory) {
      copyDirectoryCompatibilityMode(new File(fileIn, f), new File(fileOut))
    }
    else {
      Files.copy(new File(fileIn, f).toPath, new File(fileOut, f).toPath)
      logger.info("copying " + fileIn + f + " to " + fileOut)
    }
  }

  private def copyDirectoryCompatibilityMode(source: File, destination: File): Unit = {
    logger.info("open " + source)
    val cd = new CopyDirectory
    cd.copy(source.toPath.toString, destination.toPath.toString)
  }

}
