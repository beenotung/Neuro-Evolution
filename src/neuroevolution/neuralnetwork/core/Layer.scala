package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create[ValueType,WeightType](n: Int,weightGen: => WeightType): Layer = {
    val neurons = new Array[Neuron](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = Neuron.create[ValueType,WeightType](weightGen)
    new Layer(neurons)
  }
}

class Layer(val neurons: Array[Neuron]) {
  val outputs=null
  val errors=null

  def forwardConnect(layer: Layer): Unit = {
    for (neuron <- neurons)
      for (target <- layer.neurons)
        neuron.addForwardConnections(target)
  }
  def run(inputs:Array[Double]):Array[Double]={
    null
  }
}
