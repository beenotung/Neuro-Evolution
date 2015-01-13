package neuroevolution.neuralnetwork.core


import scala.collection.mutable
import scala.util.Random

/**
 * Created by beenotung on 1/11/15.
 */
object Neuron {
  val initId = BigInt(0)
  val random: Random = new Random(System.currentTimeMillis())
  var lastId = initId

  def create(weightGen: => Double): Neuron = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron(lastId, weightGen)
  }
}

class Neuron(id: BigInt, weightGen: => Double) {
  var backwardConnections = mutable.HashMap[Neuron, Double]()

  def addBackwardConnections(neuron: Neuron) = {
    backwardConnections.put(neuron, weightGen)
  }

  def run(inputs: Array[Double]): Double = {
    val weights = backwardConnections.values.toArray
    var sum: Double = 0
    for (i <- inputs.indices)
      sum += inputs(i) * weights(i)
    sum
  }
}
