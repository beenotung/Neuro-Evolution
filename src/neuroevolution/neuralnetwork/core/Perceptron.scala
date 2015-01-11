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
}

class Perceptron(val layers: Array[Layer]) {

}
