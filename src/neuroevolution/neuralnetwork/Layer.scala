package neuroevolution.neuralnetwork

import scala.collection.parallel.mutable.ParArray

/**
 * Created by beenotung on 1/11/15.
 */
object Layer {
  def create(n: Int,activationFunction: ActivationFunction): Layer = {
    new Layer(ParArray.fill[Neuron](n)(new Neuron(activationFunction)))
  }
}

class Layer(val neurons: ParArray[Neuron]) {
  val outputs = Array.fill[Double](neurons.length)(0d)

  def setNextLayer(nextLayer: Layer): Unit = {
    for (neuron <- neurons)
      neuron.setWeightNum(nextLayer.neurons.length)
  }

  def run(inputs: Array[Double]): Array[Double] = {
    Range(0, neurons.length).par.foreach(i => outputs(i) = neurons(i).run(inputs))
    outputs
  }
}
