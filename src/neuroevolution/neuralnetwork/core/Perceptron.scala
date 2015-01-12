package neuroevolution.neuralnetwork.core

import javax.security.auth.callback.Callback

/**
 * Created by beenotung on 1/11/15.
 */
object Perceptron {
  def importXml[T](filePath: String): Perceptron = {
    //TODO load xml
    new Perceptron[T](null)[T]
  }

  def exportXml(filePath: String): Unit = {
    //TODO save xml
  }

  def importDatabase[T](dbUrl: String): Perceptron = {
    //TODO load database
    new Perceptron[T](null)[T]
  }

  def exportDatabase(dbUrl: String): Unit = {
    //TODO save database
  }

  def create[T](frame: Array[Int],weightGen:()=>T): Perceptron = {
    val layers = new Array[Layer](frame.length)
    for (i <- 0 to (frame.length - 1))
      layers(i) = Layer.create(frame(i),weightGen)
    for (iLayer <- 1 to (layers.length - 1))
      for (neuron <- layers(iLayer).neurons)
        for(target<-layers(iLayer - 1).neurons)
        neuron.addForwardConnections(target)
    new Perceptron[T](layers)[T]
  }
}

class Perceptron[T](val layers: Array[Layer]) {

}
