package neuroevolution

import neuroevolution.geneticalgorithm.GA
import neuroevolution.geneticalgorithm.GA.ProblemType
import neuroevolution.geneticalgorithm.GA.ProblemType.ProblemType
import neuroevolution.neuralnetwork.Perceptron

import scala.collection.parallel.mutable.ParArray

/**
 * Created by beenotung on 3/25/15.
 */

class NeuroEvolution(n_Bit_Weight: Int, n_Bit_Bias: Int, numberOfNodes: Array[Int],
                     val popSize: Int = 32,
                     var pSelection: Double = 0.25d,
                     var pMutation: Double = 0.01d, aMutation: Double = 0.1d,
                     val problemType: ProblemType = ProblemType.Minimize,
                     get_perceptron_input: => Array[Double],
                     eval_perceptron_function: Array[Double] => Double,
                     var LOOP_INTERVAL: Long = 100)
  extends Thread {
  val bitSize: Int = Perceptron.getNumberOfWeight(numberOfNodes) * n_Bit_Weight + numberOfNodes.sum * n_Bit_Bias
  val converter: Converter = new Converter(N_BIT_WEIGHT = n_Bit_Weight, N_BIT_BIAS = n_Bit_Bias, numberOfNodes, BIT_SIZE = bitSize)
  val ga: GA = new GA(POP_SIZE = popSize, BIT_SIZE = bitSize, P_SELECTION = pSelection, P_MUTATION = pMutation, A_MUTATION = aMutation, EVAL_FITNESS_FUNCTION = evalFitness_function, PROBLEM_TYPE = problemType)

  def evalFitness_function(rawCode: ParArray[Boolean]): Double = {
    val perceptron: Perceptron = converter.decode(rawCode.toArray)
    eval_perceptron_function(perceptron.run(get_perceptron_input))
  }

  override def run = {
    setup
    while (true) {
      loop
      Thread.sleep(LOOP_INTERVAL)
    }
  }

  def loop = {
    ga.loop
  }

  def setup = {
    ga.setup
  }
}
