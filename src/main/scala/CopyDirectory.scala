import java.io.File
import java.util.Objects
import java.util.concurrent.{ExecutorService, Executors, Future}
import scala.concurrent.ExecutionException
import scala.concurrent.ExecutionContext.Implicits.global


class CopyDirectory {

  val es: ExecutorService = Executors.newFixedThreadPool(4)

  @throws[ExecutionException]
  @throws[InterruptedException]
  def copy(sourceDirectory: String, destinationDirectory: String): Unit = {
    for (f <- Objects.requireNonNull(new File(sourceDirectory).list)) {
      val callable = new CopyDirectoryThread(sourceDirectory, destinationDirectory, f)
      val tasks: Future[Unit] = es.submit(callable)
      tasks.get
    }
    es.shutdown()
  }
}
