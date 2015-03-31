package neuroevolution.geneticalgorithm


import neuroevolution.geneticalgorithm.GA.ProblemType
import neuroevolution.geneticalgorithm.GA.ProblemType.ProblemType
import neuroevolution.utils.Utils

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

class GA(POP_SIZE: Int, BIT_SIZE: Int, P_SELECTION: Double,
         P_MUTATION: Double, A_MUTATION: Double,
         EVAL_FITNESS_FUNCTION: Array[Boolean] => Double,
         PROBLEM_TYPE: ProblemType = ProblemType.Maximize,
         var LOOP_INTERVAL: Long = 100)
  extends Thread {
  var genes: Array[Gene] = new Array[Gene](POP_SIZE)

  def setup = {
    for (i <- genes.indices) {
      genes(i) = new Gene(BIT_SIZE, A_MUTATION, EVAL_FITNESS_FUNCTION)
    }
    eval
  }

  def loop = {
    select
    crossover
    mutation
    eval
  }

  def eval = {
    evalFitness
    evalDiversity
    for (gene <- genes)
      gene.eval(0.5d)
    sort
  }

  def evalFitness = {
    for (gene <- genes)
      gene.evalFitness
    evalDiversity
  }

  def evalDiversity = {
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

  def sort = {
    genes = genes.sorted
    if (PROBLEM_TYPE.equals(GA.ProblemType.Maximize)) genes = genes.reverse
  }

  def select = {
    genes = genes.sorted
    val popTotal: Double = POP_SIZE * 1D
    for (i <- genes.indices)
      genes(i).selected = (i / popTotal) <= P_SELECTION
  }

  def crossover = {
    var p1, p2 = 0
    for (gene <- genes)
      if (!gene.selected) {
        do{p1 = Utils.random.nextInt(POP_SIZE)}while(genes(p1).selected)
        do{p2 = Utils.random.nextInt(POP_SIZE)}while(genes(p1).selected|| p1.equals(p2))
        gene.crossover(genes(p1), genes(p2))
      }
  }

  def mutation = {
    for (gene <- genes)
      if (Utils.random.nextDouble() < P_MUTATION)
        gene.mutation
  }

  override def run = {
    setup
    while (true) {
      loop
      Thread.sleep(LOOP_INTERVAL)
    }
  }
  def getBestGene:Gene={
    genes(0)
  }
  def getBestRawCode:Array[Boolean]={
    getBestGene.rawCode
  }
}
