package neuroevolution.neuralnetwork.core


import scala.collection.mutable
import scala.util.Random
import myutils.Utils

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
  var bias:Float=Utils.random.nextFloat

  def addBackwardConnections(neuron: Neuron) = {
    weights+=Utils.random.nextFloat
  }

  def run(inputs: Array[Float]): Double = {
    var sum: Double = 0
    for (i <- inputs.indices)
      sum += inputs(i) * weights(i)
    sum
  }
}
