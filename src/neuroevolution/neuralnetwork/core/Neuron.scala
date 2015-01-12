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

  def create[ValueType,WeightType](weightGen:  => WeightType): Neuron = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron[ValueType,WeightType](lastId, weightGen)[WeightType]
  }

}

class Neuron[ValueType,WeightType](id: BigInt, weightGen:  => WeightType) {
  var forwardConnections = HashMap[Neuron, WeightType]()
  def addForwardConnections(neuron: Neuron) = {
    forwardConnections.put(neuron, weightGen)
  }
}
