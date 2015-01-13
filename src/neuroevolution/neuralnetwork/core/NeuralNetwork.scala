package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */

import scala.math.exp
import scala.util.Random

abstract class ActivationFunction {
  def eval(value: Double): Double
}

object Sigmoid extends ActivationFunction {
  override def eval(value: Double): Double = {
    1d / (1d + exp(-value))
  }
}

object NeuralNetworkApplication extends App {
  override def main(args: Array[String]): Unit = {
    println("start")

    menu

    println("end")
  }

  def menu: Unit = {
    println("menu")
    test
  }

  def test: Unit = {
    //TODO demo test
    val frame = Array[Int](1, 4, 4, 1)

    def weightGen: Double = {
      Random.nextDouble()
    }

    val perceptron = Perceptron.create(frame, weightGen)
  }

}
