package neuroevolution.geneticalgorithm

import neuroevolution.utils.{DataTypes, Utils}


/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int, A_MUTATION: Double = 0.1d, evalFitness_function: (Array[Boolean]) => Double) extends Comparable[Gene] {
  val rowCode: Array[Boolean] = new Array[Boolean](BIT_SIZE)
  var fitness: Double = 0D
  var diversity: Double = 1D
  var preference: Double = 0.5D
  var selected: Boolean = true



  def eval(fitnessWeight: Double, diversityWeight: Double) = {
    preference = fitness * fitnessWeight + diversity * diversityWeight
  }

  def evalFitness = {
    fitness = evalFitness_function(rowCode)
  }

  def evalDiversity(centroid: Array[Double]) = {
    diversity = 0
    for (bit <- rowCode.indices)
    //diversity+=Math.abs(rowCode(x)-centroid(x))
      diversity += Math.pow(DataTypes.-(rowCode(bit), centroid(bit)), 2)
  }

  override def compareTo(o: Gene): Int = {
    fitness.compareTo(o.preference)
  }

  def crossover(p1: Gene, p2: Gene): Unit = {
    for (i <- rowCode.indices)
      if (Utils.random.nextBoolean())
        rowCode(i) = p1.rowCode(i)
      else
        rowCode(i) = p2.rowCode(i)
  }

  def mutation: Unit = {
    for (i <- rowCode.indices)
      if (Utils.random.nextDouble() < A_MUTATION)
        rowCode(i) ^= true
  }
}


