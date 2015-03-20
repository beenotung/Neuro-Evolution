package neuroevolution

import neuroevolution.neuralnetwork.core.Perceptron

/**
 * Created by beenotung on 1/11/15.
 */
object Launcher extends App {

  def start: Unit = {
    println("Launcher start")
    test
    println("Launcher end")
  }

  def test: Unit = {
    println("test start")
    var perceptron: Perceptron = Perceptron.create_old(Array[Int](2, 2, 1))
    println("test end")
  }
}