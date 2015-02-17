package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Perceptron {
  def importXml(filePath: String): Perceptron = {
    //TODO load xml
    new Perceptron(null)
  }

  def exportXml(filePath: String): Unit = {
    //TODO save xml
  }

  def importDatabase(dbUrl: String): Perceptron = {
    //TODO load database
    new Perceptron(null)
  }

  def exportDatabase(dbUrl: String): Unit = {
    //TODO save database
  }

  def create(frame: Array[Int]): Perceptron = {
    val layers = new Array[Layer](frame.length)
    for (i <- frame.indices)
      layers(i) = Layer.create(frame(i))
    for (iLayer <- 1 to (layers.length - 1))
      for (neuron <- layers(iLayer).neurons)
        for (target <- layers(iLayer - 1).neurons)
          neuron.addBackwardConnections(target)
    new Perceptron(layers)
  }
}


class Perceptron(val layers: Array[Layer]) {
  def run(inputs: Array[Double]): Array[Double] = {
    if (inputs.length != layers(0).neurons.length)
      throw new UnsupportedOperationException
    var array: Array[Double] = inputs
    for (layer <- layers)
      array = layer.run(array)
    return array
  }
}
