package neuroevolution.neuralnetwork

/**
 * Created by beenotung on 1/11/15.
 */


class Neuron(var inputWeights: Array[Double], activationFunction: ActivationFunction) {
  var bias: Double = 0d
  var input: Double = 0d
  var output: Double = 0d

  def this(activationFunction: ActivationFunction) = {
    this(inputWeights = Array.empty[Double], activationFunction)
  }

  def setInputWeightNum(n: Int) = {
    inputWeights = Array.fill[Double](n)(0d)
  }

  def run(inputs: Array[Double]): Double = {
    input = 0
    for (i <- inputs.indices)
      input += inputs(i) * inputWeights(i)
    input = (input * 1.0 - bias) / inputWeights.length
    output = activationFunction.eval(input)
    output
  }
}
