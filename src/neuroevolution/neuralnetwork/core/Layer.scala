package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create[T](n: Int,weightGen: => T): Layer = {
    val neurons = new Array[Neuron](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = Neuron.create(weightGen)
    new Layer[T](neurons)[T]
  }
}

class Layer[T](val neurons: Array[Neuron]) {
  def forwardConnect(layer: Layer): Unit = {
    for (neuron <- neurons)
      for (target <- layer.neurons)
        neuron.addForwardConnections(target)
  }
}
