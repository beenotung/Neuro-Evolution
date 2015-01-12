package neuroevolution.neuralnetwork.core

import java.lang.Double

import scala.collection.mutable.HashMap
import scala.util.Random

/**
 * Created by beenotung on 1/11/15.
 */
 object Neuron {
  val initId = BigInt(0)
  val random: Random = new Random(System.currentTimeMillis())
  var lastId = initId

  def create[ValueType, WeightType](weightGen: => WeightType): Neuron[ValueType, WeightType] = {
    lastId = lastId + 1
    if (lastId == initId)
      throw new IndexOutOfBoundsException
    new Neuron[ValueType,WeightType](lastId, weightGen)[WeightType]
  }
}

object myutils {
  def cast[A](x: A)(implicit num: Numeric[A]): A = {
    val result = new Double(num.toDouble(x))
    (x match {
      case x: Double => result
      case x: Int => result.toInt
      case x: Float => result.toFloat
      case x: Long => result.toLong
    }).asInstanceOf[A]
  }
}

class Neuron[ValueType,WeightType](id: BigInt, weightGen:  => WeightType) {
  var forwardConnections = HashMap[Neuron[ValueType, WeightType], WeightType]()

  def addForwardConnections(neuron: Neuron[ValueType, WeightType]) = {
    forwardConnections.put(neuron, weightGen)
  }

  def run(inputs: Array[ValueType]): ValueType = {
    //var sum:ValueType=myutils.cast[ValueType](0)
    var sum = 0
    for (i <-)
      null
  }
}
