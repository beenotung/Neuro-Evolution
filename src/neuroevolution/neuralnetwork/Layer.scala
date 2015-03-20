package neuroevolution.neuralnetwork

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create(n: Int): Layer = {
    val neurons = new Array[Neuron](n)
    for (i <- 0 to (neurons.length - 1))
      neurons(i) = new Neuron
    new Layer(neurons)
  }
}

class Layer(val neurons: Array[Neuron]) {
  val outputs = new Array[Double](neurons.length)

  def setNextLayer(nextLayer:Layer): Unit ={
    for(neuron<-neurons)
      neuron.setWeightNum(nextLayer.neurons.length)
  }

  def run(inputs: Array[Double]): Array[Double] = {
    for (i <- neurons.indices)
      outputs(i) = neurons(i).run(inputs)
    outputs
  }
}
