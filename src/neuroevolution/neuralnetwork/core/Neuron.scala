package neuroevolution.neuralnetwork.core

import scala.collection.mutable.HashMap
import scala.util.Random

/**
 * Created by beenotung on 1/11/15.
 */
 object Neuron {
  val initId = BigInt(0)
  val random: Random = new Random(System.currentTimeMillis())
  var lastId = initId

  def create[T](weightGen:  => T): Neuron = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron[T](lastId, weightGen)[T]
  }

}

class Neuron[T](id: BigInt, weightGen:  => T) {
  var forwardConnections = HashMap[Neuron, T]()

  def addForwardConnections(neuron: Neuron) = {
    forwardConnections.put(neuron, weightGen)
  }
}
