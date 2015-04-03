package neuroevolution.neuralnetwork

/**
 * Created by beenotung on 1/11/15.
 */

import scala.math.exp

trait ActivationFunction {
  def eval(value: Double): Double
}

object Sigmoid extends ActivationFunction {
  override def eval(value: Double): Double = {
    1d / (1d + exp(-value))
  }
}

/**
 * input range from -1.0 to 1.0
 * output range from -1.0 to 1.0
 */
object TweakedSine extends ActivationFunction {
  val ratio = Math.PI / 2d

  override def eval(value: Double): Double = {
    Math.sin(value * ratio)
  }
}

object TweakedCosine extends ActivationFunction{
  override def eval(value: Double): Double = {
    (-Math.cos((value)*Math.PI)+1)/2
  }
}