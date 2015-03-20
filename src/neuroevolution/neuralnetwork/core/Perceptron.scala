package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */
object Perceptron {
   def create(numberOfNodes:Array[Int]):Perceptron={
    val layers = new Array[Layer](numberOfNodes.length)
    for(iLayer<-numberOfNodes.indices)
      layers(iLayer)=Layer.create(numberOfNodes(iLayer))
    for(iLayer<-0 to (layers.length -2))
      layers(iLayer).setNextLayer(layers(iLayer+1))
    new Perceptron(layers)
  }
}


class Perceptron(val layers: Array[Layer]) {
  def run(inputs: Array[Double]): Array[Double] = {
    if (inputs.length != layers(0).neurons.length)
      throw new UnsupportedOperationException
    var values: Array[Double] = inputs
    for (layer <- layers)
      values = layer.run(values)
    return values
  }
}
