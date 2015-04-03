package neuroevolution.geneticalgorithm


import java.util.concurrent.Semaphore

import neuroevolution.geneticalgorithm.ProblemType.ProblemType
import neuroevolution.utils.Utils

import scala.collection.parallel.mutable.ParArray

/**
 * Created by beenotung on 1/30/15.
 */

/*
This type of genetic algorithm find rawCode rawCode for MAXIMUM fitness
*/

object ProblemType extends Enumeration {
  type ProblemType = super.Value
  val Maximize, Minimize = Value
}

/**
 *
 * @param POP_SIZE
 * @param BIT_SIZE
 * @param P_SELECTION
 * @param P_MUTATION
 * @param A_MUTATION
 * @param EVAL_FITNESS_FUNCTION
 * @param PROBLEM_TYPE
 * @param DIVERSITY_WEIGHT
 * range from 0.0 to 1.0
 * @param LOOP_INTERVAL
 */
class GA(POP_SIZE: Int, var BIT_SIZE: Int, P_SELECTION: Double,
         P_MUTATION: Double, A_MUTATION: Double,
         PARENT_IMMUTABLE: Boolean,
         EVAL_FITNESS_FUNCTION: Array[Boolean] => Double,
         PROBLEM_TYPE: ProblemType = ProblemType.Maximize,
         DIVERSITY_WEIGHT: Double,
         var LOOP_INTERVAL: Long = 100)
  extends Thread {
  val loopSemaphore: Semaphore = new Semaphore(1)
  var genes: ParArray[Gene] = ParArray.fill[Gene](POP_SIZE)(new Gene(BIT_SIZE, A_MUTATION, EVAL_FITNESS_FUNCTION, PROBLEM_TYPE))

  def resize(newBitSize: Int) = {
    loopSemaphore.tryAcquire()
    BIT_SIZE = newBitSize
    for (gene <- genes)
      gene.resize(newBitSize)
    loopSemaphore.release()
  }

  def setup = {
    genes.foreach(gene => gene.setup)
    eval
  }

  def loop = {
    loopSemaphore.tryAcquire()
    select
    crossover
    mutation
    eval
    loopSemaphore.release()
  }

  def eval = {
    evalFitness
    evalDiversity
    for (gene <- genes)
      gene.eval(DIVERSITY_WEIGHT)
  }

  def evalFitness = {
    genes.foreach(gene => gene.evalFitness)
  }

  def evalDiversity = {
    //TODO diversity
    val centroid: ParArray[Double] = ParArray.fill[Double](BIT_SIZE)(0)

    genes.foreach(gene => Range(0, gene.rawCode.length).par.foreach(i => if (gene.rawCode(i)) centroid(i) += 1))

    Range(0, centroid.length).par.foreach(i => centroid(i) /= 1.0 * POP_SIZE)

    genes.foreach(gene => gene.evalDiversity(centroid))

    for (gene <- genes)
      gene.evalDiversity(centroid)
  }


  def select = {
    //TODO sort
    val popTotal: Double = POP_SIZE
    val fitnessList = sortByFitness
    Range(0, genes.length).par.foreach(i => fitnessList(i).selected = (i / popTotal) <= P_SELECTION * (1 - DIVERSITY_WEIGHT))
    val diversityList = sortByDiversity
    Range(0, genes.length).par.foreach(i => if ((i / popTotal) <= P_SELECTION * DIVERSITY_WEIGHT) diversityList(i).selected = true)
  }

  def crossover = {
    genes.foreach(gene => if (!gene.selected) {
      var p1, p2 = 0
      do {
        p1 = Utils.random.nextInt(POP_SIZE)
      } while (!genes(p1).selected)
      do {
        p2 = Utils.random.nextInt(POP_SIZE)
      } while (!genes(p2).selected || p1.equals(p2))
      gene.crossover(genes(p1), genes(p2))
    })
  }

  def mutation = {
    genes.foreach(gene => if ((Utils.random.nextDouble() < P_MUTATION) && !(gene.selected && PARENT_IMMUTABLE)) gene.mutation)
  }

  var round = 0

  override def run = {
    setup

    while (true) {
      loop
      round += 1
      if (LOOP_INTERVAL > 0)
        Thread.sleep(LOOP_INTERVAL)
    }
  }

  def getBestRawCode: Array[Boolean] = {
    getBestGene.rawCode
  }

  def getBestGene: Gene = {
    loopSemaphore.tryAcquire()
    val bestGene = sortByFitness.head
    loopSemaphore.release()
    bestGene
  }

  def sortedGenes(getValue: (Gene) => Double): Array[Gene] = {
    genes.toArray.sortWith((gene1, gene2) => getValue(gene1) > getValue(gene2))
  }

  def sortByFitness: Array[Gene] = {
    sortedGenes(gene => gene.fitness)
  }

  def sortByDiversity: Array[Gene] = {
    sortedGenes(gene => gene.diversity)
  }

  def sortByPreference: Array[Gene] = {
    sortedGenes(gene => gene.preference)
  }

  def getBestFitness: Double = {
    var fitness = getBestGene.fitness
    if (PROBLEM_TYPE.equals(ProblemType.Minimize))
      fitness = 1 / fitness
    fitness
  }

}
