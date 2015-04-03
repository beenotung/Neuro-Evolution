package neuroevolution.neuralnetwork

/**
 * Created by beenotung on 1/11/15.
 */
object Perceptron {
  def create(numberOfNodes: Array[Int], activationFunction: ActivationFunction): Perceptron = {
    val layers=Array.tabulate[Layer](numberOfNodes.length)(i=> Layer.create(numberOfNodes(i),activationFunction))
    Range(1,layers.length).par.foreach(i=>layers(i).setInputLayer(layers(i-1)))
    new Perceptron(layers)
  }

  def getNumberOfNodes(layers: Array[Layer]): Array[Int] = {
    val numberOfNodes: Array[Int] = new Array[Int](layers.length)
    for (i <- layers.indices)
      numberOfNodes(i) = layers(i).neurons.length
    numberOfNodes
  }

  def getNumberOfWeight(numberOfNodes: Array[Int]): Int = {
    var count: Int = 0
    for (i <- 0 to (numberOfNodes.length - 2))
      count += numberOfNodes(i) * numberOfNodes(i + 1)
    count
  }
}


class Perceptron(val layers: Array[Layer]) {
  def run(inputs: Array[Double]): Array[Double] = {
    if (inputs.length != layers(0).neurons.length)
      throw new UnsupportedOperationException
    var values=inputs
    Range(1,layers.length).foreach(i=>values=layers(i).run(values))
    values
  }
}
