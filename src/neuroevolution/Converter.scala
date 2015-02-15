package neuroevolution

import neuroevolution.neuralnetwork.core.{Layer, Neuron, Perceptron}

/**
 * Created by beenotung on 2/13/15.
 */

object Converter {

  val N_INPUT_CELL = 2
  val N_HIDDEN_CELLs: List[Int] = List[Int]()
  val N_OUTPUT_CELL = 2

  val bitDecimals = for (i <- 0 to 1024) yield i / 2f

  def Decode(rawCode: Array[Boolean], N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Perceptron = {
    var index = 0
    val layers: Array[Int] = new Array[Int](2)
    layers(0) = 2
    layers(1) = 2
    val perceptron: Perceptron = Perceptron.create(layers)
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
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
      for (neuron: Neuron <- layer) {
        neuron.bias = 0
        for (iBit <- Range(1, N_BIT_BIAS)) {
          if (rawCode(index))
            neuron.bias += bitDecimals(iBit)
          index += 1
        }
      }
    }
    perceptron
  }
}

