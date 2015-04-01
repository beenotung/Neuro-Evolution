package neuroevolution.neuralnetwork

/**
 * Created by beenotung on 1/11/15.
 */


class Neuron(var weights: Array[Double]) {
  var bias: Double = 0d
  var sum: Double = 0d
  var output: Double = 0d

  def this() = {
    this(null)
  }

  def setWeightNum(n: Int) = {
    weights = Array.fill[Double](n)(0d)
  }

  def run(inputs: Array[Double]): Double = {
    sum = 0
    for (i <- inputs.indices)
      sum += inputs(i) * weights(i)
    output = Sigmoid.eval(sum - bias)
    output
  }
}
