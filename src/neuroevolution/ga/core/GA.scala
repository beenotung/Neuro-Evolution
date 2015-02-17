package neuroevolution.ga.core

/**
 * Created by beenotung on 1/30/15.
 */
class GA(POP_SIZE: Int = 32, MATURE_ROUND: Int = 40) {
  val genes: Array[Gene] = new Array[Gene](POP_SIZE)

  def setup: Unit = {}

  def loop: Unit = {}

  def eval: Unit = {}
}
