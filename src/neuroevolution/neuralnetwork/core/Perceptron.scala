package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Perceptron {
  def importXml[ValueType, WeightType](filePath: String): Perceptron[ValueType, WeightType] = {
    //TODO load xml
    new Perceptron[ValueType, WeightType](null)
  }

  def exportXml(filePath: String): Unit = {
    //TODO save xml
  }

  def importDatabase[ValueType, WeightType](dbUrl: String): Perceptron[ValueType, WeightType] = {
    //TODO load database
    new Perceptron[ValueType, WeightType](null)[WeightType]
  }

  def exportDatabase(dbUrl: String): Unit = {
    //TODO save database
  }

  def create[ValueType, WeightType](frame: Array[Int], weightGen: () => WeightType): Perceptron[ValueType, WeightType] = {
    val layers = new Array[Layer](frame.length)
    for (i <- frame.indices)
      layers(i) = Layer.create(frame(i), weightGen)
    for (iLayer <- layers.indices)
      for (neuron <- layers(iLayer).neurons)
        for (target <- layers(iLayer - 1).neurons)
          neuron.addForwardConnections(target)
    new Perceptron[ValueType, WeightType](layers)
  }
}

class Perceptron[ValueType, WeightType](val layers: Array[Layer]) {
  def run(inputs: Array[Double]): Array[Double] = {
    if (inputs.length != layers(0).neurons.length)
      throw new UnsupportedOperationException
    var array: Array[Double] = inputs
    for (layer <- layers)
      array = layer.run(array)
    return array
  }
}
