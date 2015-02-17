package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create(n: Int): Layer = {
    val neurons = new Array[Neuron](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = Neuron.create()
    new Layer(neurons)
  }
}

class Layer(val neurons: Array[Neuron]) {
  val outputs = new Array[Float](neurons.length)
  val errors = null

  def backwardConnect(layer: Layer): Unit = {
    for (neuron <- neurons)
      for (target <- layer.neurons)
        neuron.addBackwardConnections(target)
  }

  def run(inputs: Array[Float]): Array[Float] = {
    for (i <- neurons.indices)
      outputs(i) = neurons(i).run(inputs)
    outputs
  }
}
