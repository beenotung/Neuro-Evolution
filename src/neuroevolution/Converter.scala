package neuroevolution

import neuroevolution.neuralnetwork.core.{Layer, Neuron, Perceptron}

/**
 * Created by beenotung on 2/13/15.
 */

object Converter {

  val N_INPUT_CELL = 2
  val N_HIDDEN_CELLs: List[Int] = List[Int]()
  val N_OUTPUT_CELL = 2


  def Decode(rawCode: Array[Boolean], N_BIT_WEIGHT: Int, N_BIT_BIAS: Int): Perceptron = {
    val N_BIT_WEIGHT_DIVIDER: Float = (Math.pow(2, N_BIT_WEIGHT) - 1).toFloat
    val N_BIT_BIAS_DIVIDER: Float = (Math.pow(2, N_BIT_BIAS) - 1).toFloat

    var index = 0
    var tmp = 0
    val layers: Array[Int] = new Array[Int](2)
    layers(0) = 2
    layers(1) = 2
    val perceptron: Perceptron = Perceptron.create(layers)
    //decode weight
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        for (iWeight: Int <- neuron.weights.indices) {
          tmp = 0
          for (iBit <- Range(1, N_BIT_WEIGHT)) {
            tmp *= 2
            if (rawCode(index))
              tmp = tmp + 1
            index += 1
          }
          neuron.weights(iWeight) = tmp / N_BIT_WEIGHT_DIVIDER
        }
      }
    }
    //decode bias
    for (layer: Layer <- perceptron.layers) {
      for (neuron: Neuron <- layer) {
        tmp = 0
        for (iBit <- Range(1, N_BIT_BIAS)) {
          tmp *= 2
          if (rawCode(index))
            tmp = tmp + 1
          index += 1
        }
        neuron.bias = tmp / N_BIT_BIAS_DIVIDER
      }
    }
    perceptron
  }
}

