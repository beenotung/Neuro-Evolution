package neuroevolution.utils

/**
 * Created by beenotung on 3/18/15.
 */
object DataTypes {

  def -(b: Boolean, x: Int): Int = {
    toInt(b) - x
  }

  def toInt(b: Boolean): Int = {
    if (b) 1 else 0
  }

  def -(b: Boolean, x: Double): Double = {
    toInt(b) - x
  }

  implicit class BooleanNum(var b: Boolean) {
    def toInt = if (b) 1 else 0

    def -(x: Int): Int = if (b) 1 - x else -x

    def -(x: Double): Double = if (b) 1 - x else -x
  }

}
