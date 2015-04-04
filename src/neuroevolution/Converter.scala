package neuroevolution


import java.util.concurrent.ConcurrentHashMap

import neuroevolution.Converter.bufferedPerceptrons
import neuroevolution.neuralnetwork.{ActivationFunction, Layer, Neuron, Perceptron}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by beenotung on 2/13/15.
 */

object Converter {
  val bitDecimals = for (i <- 0 to 1024) yield i / 2d
  val bufferedPerceptrons = new ConcurrentHashMap[String, Perceptron]

  def decode(rawCode: Array[Boolean], perceptron: Perceptron, N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Unit = {
    val weightIndexes = new ArrayBuffer[WeightIndex]
    val biasIndexs = new ArrayBuffer[BiasIndex]
    perceptron.layers.indices.foreach(
      iLayer => Range(0, perceptron.layers(iLayer).neurons.length).foreach(
        iNeuron => {
          perceptron.layers(iLayer).neurons(iNeuron).inputWeights.indices.foreach(
            iWeight => weightIndexes += new WeightIndex(iLayer, iNeuron, iWeight))
          biasIndexs += new BiasIndex(iLayer, iNeuron)
        }
      ))
    weightIndexes.indices.par.foreach(
      i => perceptron.layers(weightIndexes(i).iLayer).neurons(weightIndexes(i).iNeuron).inputWeights(weightIndexes(i).iWeight) =
        bitsToDouble(rawCode, i * N_BIT_WEIGHT, N_BIT_WEIGHT)
    )
    val offset = weightIndexes.length * N_BIT_WEIGHT
    biasIndexs.indices.par.foreach(
      i => perceptron.layers(biasIndexs(i).iLayer).neurons(biasIndexs(i).iNeuron).bias =
        bitsToDouble(rawCode, offset + i * N_BIT_BIAS, N_BIT_BIAS)
    )
  }

  def bitsToDouble(bits: Array[Boolean], start: Int, length: Int): Double = {
    var value: Double = 0d
    Range(0, length).foreach(i => if (bits(start + i))
      value += bitDecimals(i + 1)
    )
    value
  }

  def decode_old(rawCode: Array[Boolean], perceptron: Perceptron, N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Unit = {
    var index = 0
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        for (iWeight: Int <- neuron.inputWeights.indices) {
          neuron.inputWeights(iWeight) = 0
          for (iBit <- Range(1, N_BIT_WEIGHT)) {
            if (rawCode(index))
              neuron.inputWeights(iWeight) += bitDecimals(iBit)
            index += 1
          }
        }
      }
    }
    //decode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        neuron.bias = 0
        for (iBit <- Range(1, N_BIT_BIAS)) {
          if (rawCode(index))
            neuron.bias += bitDecimals(iBit)
          index += 1
        }
      }
    }
  }

  def encode(perceptron: Perceptron, rawCode: Array[Boolean], N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Unit = {
    val weightIndexes = new ArrayBuffer[WeightIndex]
    val biasIndexs = new ArrayBuffer[BiasIndex]
    perceptron.layers.indices.foreach(
      iLayer => Range(0, perceptron.layers(iLayer).neurons.length).foreach(
        iNeuron => {
          perceptron.layers(iLayer).neurons(iNeuron).inputWeights.indices.foreach(
            iWeight => weightIndexes += new WeightIndex(iLayer, iNeuron, iWeight))
          biasIndexs += new BiasIndex(iLayer, iNeuron)
        }
      ))
    weightIndexes.indices.par.foreach(
      i => doubleToBits(
        perceptron.layers(weightIndexes(i).iLayer).neurons(weightIndexes(i).iNeuron).inputWeights(weightIndexes(i).iWeight),
        rawCode, i * N_BIT_WEIGHT, N_BIT_WEIGHT)
    )
    val offset = weightIndexes.length * N_BIT_WEIGHT
    biasIndexs.indices.par.foreach(
      i => doubleToBits(
        perceptron.layers(biasIndexs(i).iLayer).neurons(biasIndexs(i).iNeuron).bias,
        rawCode, offset + i * N_BIT_BIAS, N_BIT_BIAS)
    )
  }

  def doubleToBits(value: Double, bits: Array[Boolean], start: Int, length: Int) = {
    var d = value
    for (iBit <- 1 to length) {
      if (d >= bitDecimals(iBit)) {
        bits(start + iBit - 1) = true
        d -= bitDecimals(iBit)
      }
      else
        bits(start + iBit - 1) = false
    }
  }

  def encode_old(perceptron: Perceptron, rawCode: Array[Boolean], N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Unit = {
    var index = 0
    var tmp: Double = 0d
    //encode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        for (weight: Double <- neuron.inputWeights) {
          tmp = weight
          for (iBit <- 1 to N_BIT_WEIGHT) {
            if (tmp > bitDecimals(iBit)) {
              rawCode(index) = true
              tmp -= bitDecimals(iBit)
            }
            else
              rawCode(index) = false
            index += 1
          }
        }
      }
    }
    //encode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        tmp = neuron.bias
        for (iBit <- 1 to N_BIT_BIAS) {
          if (tmp > bitDecimals(iBit)) {
            rawCode(index) = true
            tmp -= bitDecimals(iBit)
          }
          else
            rawCode(index) = false
          index += 1
        }
      }
    }
  }

  def getNumberOfBits(N_BIT_WEIGHT: Int, N_BIT_BIAS: Int, numberOfNodes: Array[Int]): Int = {
    var countBias: Int = 0
    var countWeight: Int = 0
    for (numberOfNode <- numberOfNodes)
      countBias += numberOfNode
    for (i <- 0 to (numberOfNodes.length - 2))
      countWeight += numberOfNodes(i) * numberOfNodes(i + 1)
    countWeight * N_BIT_WEIGHT + countBias * N_BIT_BIAS
  }

  class WeightIndex(val iLayer: Int, val iNeuron: Int, val iWeight: Int)

  class BiasIndex(val iLayer: Int, val iNeuron: Int)

}

class Converter(val N_BIT_WEIGHT: Int, val N_BIT_BIAS: Int, val NUMBER_OF_NODES: Array[Int], val BIT_SIZE: Int, activationFunction: ActivationFunction) {
  def encode(perceptron: Perceptron, rawCode: Array[Boolean]) =
    Converter.encode(perceptron, rawCode, N_BIT_WEIGHT, N_BIT_BIAS)

  def encode(perceptron: Perceptron): Array[Boolean] = {
    val rawCode: Array[Boolean] = new Array[Boolean](BIT_SIZE)
    Converter.encode(perceptron, rawCode, N_BIT_WEIGHT, N_BIT_BIAS)
    rawCode
  }

  def decode(rawCode: Array[Boolean], perceptron: Perceptron) =
    Converter.decode(rawCode, perceptron, N_BIT_WEIGHT, N_BIT_BIAS)

  def decode(rawCode: Array[Boolean]): Perceptron = {
    var perceptron: Perceptron = bufferedPerceptrons.get(rawCode.toString)
    if (perceptron == null) {
      perceptron = Perceptron.create(NUMBER_OF_NODES, activationFunction)
      bufferedPerceptrons.put(rawCode.toString, perceptron)
    }
    Converter.decode(rawCode, perceptron, N_BIT_WEIGHT, N_BIT_BIAS)
    perceptron
  }
}
