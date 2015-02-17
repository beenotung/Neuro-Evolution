package neuroevolution.neuralnetwork.core


import myutils.Utils
import neuroevolution.neuralnetwork.core_java.NeuralNetwork

import scala.collection.mutable
import scala.util.Random

/**
 * Created by beenotung on 1/11/15.
 */
object Neuron {
  val initId = BigInt(0)
  val random: Random = new Random(System.currentTimeMillis())
  var lastId = initId

  def create(): Neuron = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron(lastId)
  }
}

class Neuron(id: BigInt) {
  val weights = mutable.MutableList[Float]()
  var bias: Float =0f
  var sum:Float=0f
  var output:Float=0f

  def addBackwardConnections(neuron: Neuron) = {
    weights += Utils.random.nextFloat
  }

  def run(inputs: Array[Float]): Float = {
     sum= 0
    for (i <- inputs.indices)
      sum += inputs(i) * weights(i)
    output=Sigmoid.eval(sum-bias)
    output
  }
}
