package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create[ValueType, WeightType](n: Int, weightGen: => WeightType): Layer[ValueType, WeightType] = {
    val neurons = new Array[Neuron[ValueType, WeightType]](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = Neuron.create[ValueType,WeightType](weightGen)
    new Layer[ValueType, WeightType](neurons)
  }
}

class Layer[ValueType, WeightType](val neurons: Array[Neuron[ValueType, WeightType]]) {
  val outputs = new Array[ValueType](neurons.length)
  val errors=null

  def backwardConnect(layer: Layer): Unit = {
    for (neuron <- neurons)
      for (target <- layer.neurons)
        neuron.addBackwardConnections(target)
  }

  def run(inputs: Array[ValueType]): Array[ValueType] = {
    for (i <- neurons.indices)
      outputs(i) = neurons(i).run(inputs)
    return outputs
  }
}
