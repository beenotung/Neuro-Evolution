package neuroevolution.geneticalgorithm

import neuroevolution.geneticalgorithm.ProblemType._
import neuroevolution.utils.{DataTypes, Utils}

import scala.collection.parallel.mutable.ParArray


/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int, A_MUTATION: Double = 0.1d, evalFitness_function: (Array[Boolean]) => Double, PROBLEM_TYPE: ProblemType = ProblemType.Maximize) {
  var rawCode: Array[Boolean] = new Array[Boolean](BIT_SIZE)
  var fitness: Double = 0D
  var diversity: Double = 1D
  var preference: Double = 0.5D
  var selected: Boolean = true

  def getFitness: Double = {
    if (PROBLEM_TYPE.equals(ProblemType.Maximize))
      fitness
    else
      1 / fitness
  }

  def setup = {
    rawCode.indices.par.foreach(i => rawCode(i) = Utils.random.nextBoolean())
  }

  def resize(newBitSize: Int) = {
    val newRawCode: Array[Boolean] = Array.tabulate[Boolean](newBitSize) { (i) => if (i < rawCode.length) rawCode(i) else Utils.random.nextBoolean() }
    rawCode = newRawCode
  }

  def eval(diversityWeight: Double): Double = {
    eval(1 - diversityWeight, diversityWeight)
  }

  private def eval(fitnessWeight: Double, diversityWeight: Double): Double = {
    preference = fitness * fitnessWeight + diversity * diversityWeight
    preference
  }

  def evalFitness = {
    fitness = evalFitness_function(rawCode)
    if (PROBLEM_TYPE.equals(ProblemType.Minimize)) fitness = 1 / fitness
  }

  def evalDiversity(centroid: ParArray[Double]) = {
    diversity = 0
    for (i <- Range(0, rawCode.length))
      diversity += Math.pow(DataTypes.-(rawCode(i), centroid(i)), 2)
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
      if (Utils.random.nextDouble() < A_MUTATION) rawCode(i) ^= true
    )
  }
}


