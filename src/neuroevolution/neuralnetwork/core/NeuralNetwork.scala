package neuroevolution.neuralnetwork.core

/**
 * Created by beenotung on 1/11/15.
 */

import scala.math.exp

abstract class ActivationFunction {
  def eval(value: Double): Double
}

object Sigmoid extends ActivationFunction {
  override def eval(value: Double): Double = {
    1d / (1d + exp(-value))
  }
}