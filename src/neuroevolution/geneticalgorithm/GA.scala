package neuroevolution.geneticalgorithm

import neuroevolution.utils.Utils

/**
 * Created by beenotung on 1/30/15.
 */
/*
This type of genetic algorithm find rawCode rawCode for MAXIMUM fitness
*/
class GA(POP_SIZE: Int = 32, P_SELECTION: Double = 0.25d, P_MUTATION: Double = 0.01d, BIT_SIZE: Int, A_MUTATION: Double = 0.1d, var evalFitness_function: Array[Boolean] => Double, @deprecated MATURE_ROUND: Int = 40) extends Thread {
  var genes: Array[Gene] = new Array[Gene](POP_SIZE)
  var requested: Boolean = false
  var bestRawCode: Array[Boolean] = null
  var millis: Long = 1L
  var nanos: Int = 0

  def this(bitSize: Int, evalFitnessFunction: Array[Boolean] => Double) =
    this(POP_SIZE = 32, P_SELECTION = 0.25d, P_MUTATION = 0.01d, BIT_SIZE = bitSize, A_MUTATION = 0.1d, evalFitness_function = evalFitnessFunction, MATURE_ROUND = 40)

  override def start = {
    setup
    super.start
  }

  def setup: Unit = {
    for (i <- genes.indices) {
      genes(i) = new Gene(BIT_SIZE, A_MUTATION, evalFitness_function)
    }
  }

  def request = {
    requested = true
  }

  override def run: Unit = {
    while (true) {
      run
      if (requested) {
        bestRawCode = genes(0).rawCode.clone()
        requested = false
      }
      Thread.sleep(millis, nanos)
    }
  }

  def loop: Unit = {
    eval
    select
    crossover
    mutation
  }

  def eval(): Unit = {
    evalFitness
    evalDiversity
    for (gene <- genes)
      gene.eval(0.5D, 0.5D)
  }

  def evalFitness: Unit = {
    for (gene <- genes)
      gene.evalFitness
    evalDiversity
  }

  def evalDiversity: Unit = {
    //TODO diversity
    val centroid: Array[Double] = new Array[Double](BIT_SIZE)

    for (bit <- centroid.indices)
      centroid(bit) = 0

    for (gene <- genes)
      for (bit <- centroid.indices)
        if (gene.rawCode(bit))
          centroid(bit) += 1

    for (bit <- centroid.indices)
      centroid(bit) /= POP_SIZE

    for (gene <- genes)
      gene.evalDiversity(centroid)
  }

  def select = {
    genes = genes.sorted
    val popTotal: Double = POP_SIZE * 1D
    for (i <- genes.indices)
      genes(i).selected = (i / popTotal) < P_SELECTION
  }

  def crossover: Unit = {
    var p1, p2: Int = 0
    for (gene <- genes)
      if (!gene.selected) {
        do p1 = Utils.random.nextInt(POP_SIZE) while (!genes(p1).selected)
        do p2 = Utils.random.nextInt(POP_SIZE) while ((!genes(p2).selected) || (p1 == p2))
        gene.crossover(genes(p1), genes(p2))
      }
  }

  def mutation: Unit = {
    for (gene <- genes)
      if (Utils.random.nextDouble() < P_MUTATION)
        gene.mutation
  }
}

abstract class EvalFitnessFunction {
  def evalFitness(rawCodes: Array[Boolean]): Double
}

object BitCounter extends EvalFitnessFunction {
  override def evalFitness(rawCodes: Array[Boolean]): Double = {
    var count = 0
    for (bit <- rawCodes)
      if (bit) count += 1
    count
  }
}