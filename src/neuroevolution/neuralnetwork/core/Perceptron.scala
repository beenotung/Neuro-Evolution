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
    for (i <- 0 to (frame.length - 1))
      layers(i) = Layer.create(frame(i))
    for (iLayer <- 1 to (layers.length - 1))
      for (neuron <- layers(iLayer).neurons)
        neuron.link(layers(iLayer - 1).neurons)
    new Perceptron(layers)
  }
}

class Perceptron(val layers: Array[Layer]) {

}
