package neuroevolution.ga.core

/**
 * Created by 13058536A on 2/17/2015.
 */
class What(val rawCode: Array[Boolean], val N_BIT_WEIGHT: Int, val N_BIT_BIAS: Int) {
  val genes: Array[What] = new Array[What](32)
}
