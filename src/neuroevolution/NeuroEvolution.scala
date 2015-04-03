package neuroevolution

import neuroevolution.geneticalgorithm.ProblemType.ProblemType
import neuroevolution.geneticalgorithm.{GA, ProblemType}
import neuroevolution.neuralnetwork.{ActivationFunction, Perceptron}

/**
 * @param eval_perceptron_function
 * parameter
 * [Double]: input of perceptron
 * [Double]: output of perceptron
 * result
 * Double: score of the perceptron
 */

class NeuroEvolution(n_Bit_Weight: Int, n_Bit_Bias: Int, numberOfNodes: Array[Int], activationFunction: ActivationFunction,
                     get_perceptron_input: => Array[Double], eval_perceptron_function: (Array[Double], Array[Double]) => Double,
                     val popSize: Int = 32, var pSelection: Double = 0.25d, var pMutation: Double = 0.01d, aMutation: Double = 0.1d,
                     val problemType: ProblemType = ProblemType.Minimize,
                     var LOOP_INTERVAL: Long = 100)
  extends Thread {
  val bitSize: Int = Perceptron.getNumberOfWeight(numberOfNodes) * n_Bit_Weight + numberOfNodes.sum * n_Bit_Bias
  val converter: Converter = new Converter(N_BIT_WEIGHT = n_Bit_Weight, N_BIT_BIAS = n_Bit_Bias, numberOfNodes, BIT_SIZE = bitSize, activationFunction)
  val ga: GA = new GA(POP_SIZE = popSize, BIT_SIZE = bitSize, P_SELECTION = pSelection, P_MUTATION = pMutation, A_MUTATION = aMutation, EVAL_FITNESS_FUNCTION = evalFitness_function, PROBLEM_TYPE = problemType)

  def evalFitness_function(rawCode: Array[Boolean]): Double = {
    val perceptron: Perceptron = converter.decode(rawCode)
    val input: Array[Double] = get_perceptron_input
    eval_perceptron_function(input, perceptron.run(input))
  }

  def getBestPerceptron = {
    converter.decode(ga.getBestGene.rawCode)
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
