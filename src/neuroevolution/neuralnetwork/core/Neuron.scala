package neuroevolution.neuralnetwork.core

import scala.collection.mutable.HashSet

/**
 * Created by beenotung on 1/11/15.
 */
object Neuron {
  val initId = BigInt(0)
  var lastId = initId

  def create: Neuron = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron(lastId)
  }
}

class Neuron(id: BigInt) {
  var forwardConnections = HashSet[Neuron]()
}
