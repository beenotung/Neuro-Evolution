package neuroevolution.geneticalgorithm


import java.io.{File, FileOutputStream, ObjectOutputStream}
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
 * @param P_MUTATION_POW
 * @param A_MUTATION_POW
 * @param EVAL_FITNESS_FUNCTION
 * @param PROBLEM_TYPE
 * @param diversityWeight
 * range from 0.0 to 1.0
 * @param LOOP_INTERVAL
 */
class GA(POP_SIZE: Int, var BIT_SIZE: Int, P_SELECTION: Double,
         P_MUTATION_POW: Double, A_MUTATION_POW: Double,
         PARENT_IMMUTABLE: Boolean,
         EVAL_FITNESS_FUNCTION: Array[Boolean] => Double,
         PROBLEM_TYPE: ProblemType = ProblemType.Maximize,
         var LOOP_INTERVAL: Long)
  extends Thread {
  val loopSemaphore: Semaphore = new Semaphore(1)
  val centroid: ParArray[Double] = ParArray.fill[Double](BIT_SIZE)(0)
  var genes: ParArray[Gene] = ParArray.fill[Gene](POP_SIZE)(new Gene(BIT_SIZE, EVAL_FITNESS_FUNCTION, PROBLEM_TYPE))
  var overallDiversity: Double = 0.5d
  var round = 0
  var diversityWeight: Double = 0.5d

  def resize(newBitSize: Int) = {
    loopSemaphore.acquire()
    BIT_SIZE = newBitSize
    for (gene <- genes)
      gene.resize(newBitSize)
    loopSemaphore.release()
  }

  def setup = {
    round = 0
    genes.foreach(gene => gene.setup)
    eval
  }

  def loop = {
    loopSemaphore.acquire()
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
      gene.eval(diversityWeight)
  }

  def evalFitness = {
    genes.foreach(gene => gene.evalFitness)
  }

  def evalDiversity = {
    //TODO diversity
    Range(0, centroid.length).par.foreach(i => centroid(i) = 0d)

    genes.foreach(gene => Range(0, gene.rawCode.length).par.foreach(i => if (gene.rawCode(i)) centroid(i) += 1))

    Range(0, centroid.length).par.foreach(i => centroid(i) /= 1.0 * POP_SIZE)

    genes.foreach(gene => gene.evalDiversity(centroid))

    for (gene <- genes)
      gene.evalDiversity(centroid)

    overallDiversity = 0d
    Range(0, centroid.length).foreach(i => overallDiversity += centroid(i))
    overallDiversity = 1 - overallDiversity / centroid.length
    diversityWeight = 1 - overallDiversity
  }

  def select = {
    //TODO sort
    val popTotal: Double = POP_SIZE
    val fitnessList = sortByFitness
    Range(0, genes.length).par.foreach(i => fitnessList(i).selected = (i / popTotal) <= P_SELECTION * (1 - diversityWeight))
    fitnessList.head.selected = true
    val diversityList = sortByDiversity
    Range(0, genes.length).par.foreach(i => if ((i / popTotal) <= P_SELECTION * diversityWeight) diversityList(i).selected = true)
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
    //genes.foreach(gene => if ((Utils.random.nextDouble() < P_MUTATION) && (!gene.selected || !PARENT_IMMUTABLE)) gene.mutation)
    genes.foreach(gene => if ((Utils.random.nextDouble() < Math.pow(overallDiversity, P_MUTATION_POW)) && (!gene.selected || !PARENT_IMMUTABLE))
      gene.mutation(Math.pow(overallDiversity, A_MUTATION_POW)))
  }

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

  def sortByDiversity: Array[Gene] = {
    sortedGenes(gene => gene.diversity)
  }

  def sortByPreference: Array[Gene] = {
    sortedGenes(gene => gene.preference)
  }

  def sortedGenes(getValue: (Gene) => Double): Array[Gene] = {
    genes.toArray.sortWith((gene1, gene2) => getValue(gene1) > getValue(gene2))
  }

  def getBestFitness: Double = {
    var fitness = getBestGene.fitness
    if (PROBLEM_TYPE.equals(ProblemType.Minimize))
      fitness = 1 / fitness
    fitness
  }

  def saveAllToFile(filename: String, isRaw: Boolean) = {
    loopSemaphore.acquire()
    val rawCodes = Array.tabulate[Boolean](genes.length, genes(0).rawCode.length)((g, c) => genes(g).rawCode(c))
    loopSemaphore.release()
    saveToFile(rawCodes, filename, isRaw)
  }

  def saveToFile(rawCodes: Array[Array[Boolean]], filename: String, isRaw: Boolean) = {
    if (isRaw)
      saveRawToFile(rawCodes, filename)
    else
      saveStringToFile(rawCodes, new File(filename + ".perceptron.gene"))
  }

  def saveRawToFile(rawCodes: Array[Array[Boolean]], filename: String) = {
    Utils.printToFile(new File(filename + ".perceptron.gene.ini")) { p => p.println(getIniString) }
    val out = new ObjectOutputStream(new FileOutputStream(filename + ".perceptron.gene.raw"))
    rawCodes.foreach(rawCode => out.writeObject(rawCode))
    out.close()
  }

  def saveStringToFile(rawCodes: Array[Array[Boolean]], file: File) = {
    Utils.printToFile(file) { p =>
      p.println(getIniString)
      p.println()
      rawCodes.foreach(rawCode => {
        rawCode.foreach(b =>
          if (b) p.print(1)
          else p.print(0)
        )
        p.println()
      })
    }
  }

  def getIniString: String = {
    var message = "POP_SIZE"
    message += "\n" + POP_SIZE
    message += "\n" + "BIT_SIZE"
    message += "\n" + BIT_SIZE
    message += "\n" + "P_SELECTION"
    message += "\n" + P_SELECTION
    message += "\n" + "P_MUTATION_POW"
    message += "\n" + P_MUTATION_POW
    message += "\n" + "A_MUTATION_POW"
    message += "\n" + A_MUTATION_POW
    message += "\n" + "PARENT_IMMUTABLE"
    message += "\n" + PARENT_IMMUTABLE
    message += "\n" + "ProblemType"
    message += "\n" + PROBLEM_TYPE
    message
  }

  def saveBestToFile(filename: String, isRaw: Boolean) = {
    saveToFile(Array(getBestGene.rawCode), filename, isRaw)
  }

  def getBestGene: Gene = {
    loopSemaphore.acquire()
    val bestGene = sortByFitness.head
    loopSemaphore.release()
    bestGene
  }

  def sortByFitness: Array[Gene] = {
    sortedGenes(gene => gene.fitness)
  }

}
