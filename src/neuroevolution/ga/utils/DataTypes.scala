package neuroevolution.ga.utils

/**
 * Created by beenotung on 3/18/15.
 */
object DataTypes {

  implicit class BooleanNum(b: Boolean) extends AnyVal {
    def toInt = if (b) 1 else 0

    def -(x: Int) = if (b) 1 - x else -x

    def -(x: Double) = if (b) 1 - x else -x
  }

}
