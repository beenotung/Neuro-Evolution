package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create(n: Int): Layer = {
    val neurons = new Array[Neuron](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = Neuron.create
    new Layer(neurons)
  }
}

class Layer(val neurons: Array[Neuron]) {
  def forwardConnect(layer: Layer): Unit = {
    for (neuron <- neurons)
      for ()
  }
}
