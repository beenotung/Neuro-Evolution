package neuroevolution.geneticalgorithm

import neuroevolution.utils.{DataTypes, Utils}

import scala.collection.parallel.mutable.ParArray


/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int, A_MUTATION: Double = 0.1d, evalFitness_function: (ParArray[Boolean]) => Double)
  extends Comparable[Gene] {
  var rawCode: ParArray[Boolean] = new ParArray[Boolean](BIT_SIZE)
  var fitness: Double = 0D
  var diversity: Double = 1D
  var preference: Double = 0.5D
  var selected: Boolean = true

  def resize(newBitSize: Int) = {
    val newRawCode: ParArray[Boolean] = ParArray.tabulate[Boolean](newBitSize) { (i) => if (i < rawCode.length) rawCode(i) else Utils.random.nextBoolean() }
    rawCode = newRawCode
  }

  def eval(fitnessWeight: Double): Double = {
    eval(fitnessWeight, 1 - fitnessWeight)
  }

  private def eval(fitnessWeight: Double, diversityWeight: Double): Double = {
    preference = fitness * fitnessWeight + diversity * diversityWeight
    preference
  }

  def evalFitness = {
    fitness = evalFitness_function(rawCode)
  }

  def evalDiversity(centroid: ParArray[Double]) = {
    diversity = 0
    for (i <- Range(0, rawCode.length))
      diversity += Math.pow(DataTypes.-(rawCode(i), centroid(i)), 2)
  }

  override def compareTo(o: Gene): Int = {
    fitness.compareTo(o.preference)
  }

  def crossover(p1: Gene, p2: Gene): Unit = {
    Range(0, rawCode.length).par.foreach(i =>
      if (Utils.random.nextBoolean())
        rawCode(i) = p1.rawCode(i)
      else
        rawCode(i) = p2.rawCode(i))
  }

  def mutation: Unit = {
    Range(0, rawCode.length).par.foreach(i =>
      if(Utils.random.nextDouble()<A_MUTATION) rawCode(i)^=true
    )
  }
}


