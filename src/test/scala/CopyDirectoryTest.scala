import org.scalatest.funsuite.AnyFunSuite

import java.io.{File, IOException}
import java.nio.file.{Files, Paths}
import java.util.Objects

object CopyDirectoryTest

class CopyDirectoryTest extends AnyFunSuite {

  val copyDirectory = new CopyDirectory

  val pathIn: String = "test-file/test-in"
  val pathOut1: String = Files.createTempDirectory("copyTest").toString
  val pathOut2: String = Files.createTempDirectory("copyTest").toString
  val pathOut3: String = Files.createTempDirectory("copyTest").toString

  test("Test1") {
    copyDirectory.copy(pathIn + "/folder1", pathOut1)
    assertCopied(pathIn + "/folder1", pathOut1)
    deleteFiles(new File(pathOut1))
    Files.delete(Paths.get(pathOut1))
  }

  test("Test2") {
    copyDirectory.copy(pathIn + "/folder2", pathOut2)
    assertCopied(pathIn + "/folder2", pathOut2)
    deleteFiles(new File(pathOut2))
    Files.delete(Paths.get(pathOut2))
  }

  test("Test3") {
    copyDirectory.copy(pathIn, pathOut3)
    assertCopied(pathIn, pathOut3)
    deleteFiles(new File(pathOut3))
    Files.delete(Paths.get(pathOut3))
  }

  private def assertCopied(sourceDirectoryPath: String, destinationDirectoryPath: String): Unit = { // test
    val sourceDirectory = new File(sourceDirectoryPath)
    val destinationDirectory = new File(destinationDirectoryPath)
    for (f <- Objects.requireNonNull(sourceDirectory.list)) {
      if (new File(sourceDirectory, f).isDirectory) assertCopied(new File(sourceDirectory, f).toString, destinationDirectory.toString)
      else if (!new File(destinationDirectory, f).exists) fail(f + " did not exist in destination!")
    }
  }

  @throws[IOException]
  private def deleteFiles(file: File): Unit = { // Delete all files in a folder [test]
    for (f <- Objects.requireNonNull(file.list)) {
      Files.delete(new File(file, f).toPath)
    }
  }

}



