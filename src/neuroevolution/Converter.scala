package neuroevolution

import neuroevolution.ga.core.Gene
import neuroevolution.neuralnetwork.core.{Layer, Neuron, Perceptron}

/**
 * Created by beenotung on 2/13/15.
 */

object Converter {

  val N_INPUT_CELL = 2
  val N_HIDDEN_CELLs: List[Int] = List[Int]()
  val N_OUTPUT_CELL = 2

  val bitDecimals = for (i <- 0 to 1024) yield i / 2f

  def Decode(gene: Gene, perceptron: Perceptron): Unit = {
    var index = 0
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        for (iWeight: Int <- neuron.weights.indices) {
          neuron.weights(iWeight) = 0
          for (iBit <- Range(1, gene.N_BIT_WEIGHT)) {
            if (gene.rawCode(index))
              neuron.weights(iWeight) += bitDecimals(iBit)
            index += 1
          }
        }
      }
    }
    //decode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        neuron.bias = 0
        for (iBit <- Range(1, gene.N_BIT_BIAS)) {
          if (gene.rawCode(index))
            neuron.bias += bitDecimals(iBit)
          index += 1
        }
      }
    }
  }

  def Encode(perceptron: Perceptron, gene: Gene): Unit = {
    var index = 0
    var tmp: Float = 0f
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        for (weight: Float <- neuron.weights) {
          tmp = weight
          for (iBit <- Range(1, gene.N_BIT_WEIGHT)) {
            if (tmp > bitDecimals(iBit)) {
              gene.rawCode(index) = true
              tmp -= bitDecimals(iBit)
            }
            else
              gene.rawCode(index) = false
            index += 1
          }
        }
      }
    }
    //decode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        tmp = neuron.bias
        for (iBit <- Range(1, gene.N_BIT_BIAS)) {
          if (tmp > bitDecimals(iBit)) {
            gene.rawCode(index) = true
            tmp -= bitDecimals(iBit)
          }
          else
            gene.rawCode(index) = false
          index += 1
        }
      }
    }
  }
}

