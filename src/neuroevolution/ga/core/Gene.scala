package neuroevolution.ga.core

/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int,eval_function:(Array[Boolean])=>Double) {
  val bits: Array[Boolean] = new Array[Boolean](BIT_SIZE)
  var fitness: Double = 0D
  def eval={
    fitness=eval_function(bits)
  }
}
