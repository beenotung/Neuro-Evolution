package neuroevolution

import neuroevolution.geneticalgorithm.GA
import neuroevolution.geneticalgorithm.GA.ProblemType
import neuroevolution.geneticalgorithm.GA.ProblemType.ProblemType
import neuroevolution.neuralnetwork.Perceptron

/**
 * Created by beenotung on 3/25/15.
 */

class NeuroEvolution(n_Bit_Weight: Int, n_Bit_Bias: Int, numberOfNodes: Array[Int],
                     popSize: Int = 32,
                     pSelection: Double = 0.25d,
                     pMutation: Double = 0.01d, aMutation: Double = 0.1d,
                     problemType: ProblemType = ProblemType.Minimize,
                     getInputsTargets: => (Array[Double], Array[Double])                   ) {
  val bitSize: Int = Perceptron.getNumberOfWeight(numberOfNodes) * n_Bit_Weight + numberOfNodes.sum * n_Bit_Bias
  val converter: Converter = new Converter(N_BIT_WEIGHT = n_Bit_Weight, N_BIT_BIAS = n_Bit_Bias, numberOfNodes, BIT_SIZE = bitSize)
  val ga: GA = new GA(POP_SIZE = popSize, BIT_SIZE = bitSize, P_SELECTION = pSelection, P_MUTATION = pMutation, A_MUTATION = aMutation, EVAL_FITNESS_FUNCTION = evalFitness_function, PROBLEM_TYPE = problemType)

  def evalFitness_function(rawCode: Array[Boolean]): Double = {
    val perceptron: Perceptron = converter.decode(rawCode)
    var inputs: Array[Double] = null
    var targets: Array[Double] = null
    (inputs, targets) = getInputsTargets
    val outputs = perceptron.run(inputs)
    var difference: Double = 0d
    var tmp: Double = 0d
    for (i <- targets.indices) {
      tmp = targets(i) - outputs(i)
      difference += tmp * tmp
    }
    difference
  }
}
