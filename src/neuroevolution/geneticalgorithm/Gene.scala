package neuroevolution.geneticalgorithm

import neuroevolution.utils.{DataTypes, Utils}


/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int, A_MUTATION: Double = 0.1d, evalFitness_function: (Array[Boolean]) => Double) extends Comparable[Gene] {
  val rawCode: Array[Boolean] = new Array[Boolean](BIT_SIZE)
  var fitness: Double = 0D
  var diversity: Double = 1D
  var preference: Double = 0.5D
  var selected: Boolean = true



  def eval(fitnessWeight: Double, diversityWeight: Double) = {
    preference = fitness * fitnessWeight + diversity * diversityWeight
  }

  def evalFitness = {
    fitness = evalFitness_function(rawCode)
  }

  def evalDiversity(centroid: Array[Double]) = {
    diversity = 0
    for (bit <- rawCode.indices)
    //diversity+=Math.abs(rawCode(x)-centroid(x))
      diversity += Math.pow(DataTypes.-(rawCode(bit), centroid(bit)), 2)
  }

  override def compareTo(o: Gene): Int = {
    fitness.compareTo(o.preference)
  }

  def crossover(p1: Gene, p2: Gene): Unit = {
    for (i <- rawCode.indices)
      if (Utils.random.nextBoolean())
        rawCode(i) = p1.rawCode(i)
      else
        rawCode(i) = p2.rawCode(i)
  }

  def mutation: Unit = {
    for (i <- rawCode.indices)
      if (Utils.random.nextDouble() < A_MUTATION)
        rawCode(i) ^= true
  }
}


