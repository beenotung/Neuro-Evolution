package neuroevolution

import neuroevolution.neuralnetwork.{Layer, Neuron, Perceptron}

/**
 * Created by beenotung on 2/13/15.
 */

object Converter {
  val bitDecimals = for (i <- 0 to 1024) yield i / 2d

  def decode(rawCode: Array[Boolean], perceptron: Perceptron, N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Unit = {
    var index = 0
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        for (iWeight: Int <- neuron.weights.indices) {
          neuron.weights(iWeight) = 0
          for (iBit <- Range(1, N_BIT_WEIGHT)) {
            if (rawCode(index))
              neuron.weights(iWeight) += bitDecimals(iBit)
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
    var index = 0
    var tmp: Double = 0d
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer.neurons) {
        for (weight: Double <- neuron.weights) {
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
    //decode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
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
}

class Converter(val N_BIT_WEIGHT: Int, val N_BIT_BIAS: Int, val NUMBER_OF_NODES: Array[Int],val BIT_SIZE:Int) {
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
    val perceptron: Perceptron = Perceptron.create(NUMBER_OF_NODES)
    Converter.decode(rawCode, perceptron, N_BIT_WEIGHT, N_BIT_BIAS)
    perceptron
  }
}
