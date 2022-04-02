import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.TableDrivenPropertyChecks._

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.Objects
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CopyDirectoryTest

class CopyDirectoryTest extends AnyFunSuite {

  val copyDirectory = new CopyDirectory

  val pathIn: String = "test-file/test-in"
  val pathOut: String = Files.createTempDirectory("copyTest").toString

  test("Test") {
    forAll(
      Table(
        "srcFolder",
        "/folder1",
        "/folder2",
        "/"
      )
    ) { f =>
      Await.ready(copyDirectory.copy(pathIn + f, pathOut), Duration("1 day"))
      assertCopied(pathIn + f, pathOut)
      deleteFiles(new File(pathOut))
    }
    Files.delete(Paths.get(pathOut))
  }

  private def assertCopied(sourceDirectoryPath: String, destinationDirectoryPath: String): Unit = { // test
    val sourceDirectory = new File(sourceDirectoryPath)
    val destinationDirectory = new File(destinationDirectoryPath)
    for (f <- Objects.requireNonNull(sourceDirectory.list)) {
      if (new File(sourceDirectory, f).isDirectory) assertCopied(new File(sourceDirectory, f).toString, destinationDirectory.toString)
      else if (!new File(destinationDirectory, f).exists) fail(f + " did not exist in destination!")
    }
  }

  private def deleteFiles(file: File): Unit = // Delete all files in a folder [test]
    for (f <- file.list()) {
      Files.delete(new File(file, f).toPath)
    }

}



