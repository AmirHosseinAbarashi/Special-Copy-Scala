import java.io.{File, IOException}
import java.nio.file.Files
import java.util.concurrent.Callable
import java.util.logging.Logger
import scala.concurrent.ExecutionException

class CopyDirectoryThread extends Callable[Unit] {

  private val logger = Logger.getLogger(getClass.getName)
  private var fileIn: String = _
  private var fileOut: String = _
  private var f: String = _

  def this(fileIn: String, fileOut: String, f: String) {
    this()
    this.fileIn = fileIn
    this.fileOut = fileOut
    this.f = f
  }


  @throws[Exception]
  override def call: Unit = {
    if (new File(fileIn, f).isDirectory) try copyDirectoryCompatibilityMode(new File(fileIn, f), new File(fileOut))
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
    else try {
      Files.copy(new File(fileIn, f).toPath, new File(fileOut, f).toPath)
      logger.info("copying " + fileIn + f + " to " + fileOut)
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  @throws[IOException]
  @throws[ExecutionException]
  @throws[InterruptedException]
  private def copyDirectoryCompatibilityMode(source: File, destination: File): Unit = {
    logger.info("open " + source)
    val cd = new CopyDirectory
    cd.copy(source.toPath.toString, destination.toPath.toString)
  }


}
