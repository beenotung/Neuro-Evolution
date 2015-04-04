package neuroevolution.utils

import java.io.File

import scala.util.Random

/**
 * Created by beenotung on 3/18/15.
 */
object Utils {
  val random: Random = new Random(System.currentTimeMillis)

  def printToFile(filename: String)(op: java.io.PrintWriter => Unit): Unit = {
    printToFile(new File(filename))(op)
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }
}
