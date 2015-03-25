package neuroevolution.neuralnetwork

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

  def getNumberOfNodes(layers: Array[Layer]):Array[Int]={
    val numberOfNodes:Array[Int]=new Array[Int](layers.length)
    for(i<-layers.indices)
      numberOfNodes(i)=layers(i).neurons.length
    numberOfNodes
  }
  def getNumberOfWeight(numberOfNodes: Array[Int]):Int={
    var count:Int=0
    for(i<-0 to (numberOfNodes.length -2))
      count+=numberOfNodes(i)*numberOfNodes(i+1)
    count
  }
}


class Perceptron(val layers: Array[Layer]) {
  def run(inputs: Array[Double]): Array[Double] = {
    if (inputs.length != layers(0).neurons.length)
      throw new UnsupportedOperationException
    var values: Array[Double] = inputs
    for (layer <- layers)
      values = layer.run(values)
    values
  }
}
