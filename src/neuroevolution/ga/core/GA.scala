package neuroevolution.ga.core

/**
 * Created by beenotung on 1/30/15.
 */
class GA(POP_SIZE: Int = 32, P_SELECTION: Double = 0.25d, P_MUTATION: Double = 0.01d, BIT_SIZE: Int, A_MUTATION: Double = 0.1d, eval_function: (Array[Boolean]) => Double, @deprecated MATURE_ROUND: Int = 40) extends Thread {
  val genes: Array[Gene] = new Array[Gene](POP_SIZE)

  def setup: Unit = {
    for (i <- genes.indices) {
      genes(i) = new Gene(BIT_SIZE, A_MUTATION, eval_function)
    }
  }

  def loop: Unit = {
    eval
    select
    crossover
    mutation
  }

  def eval = {
    for (gene <- genes)
      gene.eval
    evalDiversity
  }

  def evalDiversity = {
    //TODO diversity
    val centroid: Array[Double] = new Array[Double](BIT_SIZE)

    for (bit <- centroid.indices)
      centroid(bit) = 0;

    for (gene <- genes)
      for (bit <- centroid.indices)
        if (gene.bits(bit))
          centroid(bit) += 1

    for (bit <- centroid.indices)
      centroid(bit) /=POP_SIZE;

    for (gene <- genes)
      gene.evalDiversity(centroid)
  }

  def select = {}

  def crossover = {}

  def mutation: Unit = {}


  class DiversityOption(var rate: Double) {
    var successCount: Int = 0
  }

}
