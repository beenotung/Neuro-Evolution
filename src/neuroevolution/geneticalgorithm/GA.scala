package neuroevolution.geneticalgorithm


import java.util.concurrent.Semaphore

import neuroevolution.geneticalgorithm.GA.ProblemType
import neuroevolution.geneticalgorithm.GA.ProblemType.ProblemType
import neuroevolution.utils.Utils

import scala.collection.parallel.mutable.ParArray

/**
 * Created by beenotung on 1/30/15.
 */

/*
This type of genetic algorithm find rawCode rawCode for MAXIMUM fitness
*/

object GA {

  object ProblemType extends Enumeration {
    type ProblemType = super.Value
    val Maximize, Minimize = Value
  }

}

class GA(POP_SIZE: Int, var BIT_SIZE: Int, P_SELECTION: Double,
         P_MUTATION: Double, A_MUTATION: Double,
         EVAL_FITNESS_FUNCTION: Array[Boolean] => Double,
         PROBLEM_TYPE: ProblemType = ProblemType.Maximize,
         var LOOP_INTERVAL: Long = 100)
  extends Thread {
  val loopSemaphore: Semaphore = new Semaphore(1)
  var genes: ParArray[Gene] = ParArray.fill[Gene](POP_SIZE)(new Gene(BIT_SIZE, A_MUTATION, EVAL_FITNESS_FUNCTION))

  def resize(newBitSize: Int) = {
    loopSemaphore.tryAcquire()
    BIT_SIZE = newBitSize
    for (gene <- genes)
      gene.resize(newBitSize)
    loopSemaphore.release()
  }

  def setup = {
    eval
    sort
  }

  def loop = {
    loopSemaphore.tryAcquire()
    select
    crossover
    mutation
    eval
    sort
    loopSemaphore.release()
  }

  def eval = {
    evalFitness
    evalDiversity
    for (gene <- genes)
      gene.eval(0.5d)
  }

  def evalFitness = {
    genes.foreach(gene => gene.evalFitness)
  }

  def evalDiversity = {
    //TODO diversity
    val centroid: ParArray[Double] = ParArray.fill[Double](BIT_SIZE)(0)

    genes.foreach(gene => Range(0, gene.rawCode.length).par.foreach(i => if (gene.rawCode(i)) centroid(i) += 1))

    Range(0, centroid.length).par.foreach(i => centroid(i) /= POP_SIZE)

    genes.foreach(gene => gene.evalDiversity(centroid))


    for (gene <- genes)
      gene.evalDiversity(centroid)
  }

  def sort = {
    var sortedGenes: Array[Gene] = genes.toArray[Gene].sorted
    if (PROBLEM_TYPE.equals(GA.ProblemType.Maximize)) sortedGenes = sortedGenes.reverse
    genes = sortedGenes.toVector.toParArray
  }

  def select = {
    val popTotal: Double = POP_SIZE * 1D
    Range(0, genes.length).par.foreach(i => genes(i).selected = (i / popTotal) <= P_SELECTION)
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
    genes.foreach(gene => if (Utils.random.nextDouble() < P_MUTATION) gene.mutation)
  }

  override def run = {
    setup
    while (true) {
      loop
      Thread.sleep(LOOP_INTERVAL)
    }
  }

  def getBestRawCode: Array[Boolean] = {
    getBestGene.rawCode
  }

  def getBestGene: Gene = {
    genes.head
  }
}
