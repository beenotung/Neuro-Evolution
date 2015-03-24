import neuroevolution.geneticalgorithm.{BitCounter, GA}
import neuroevolution.neuralnetwork.{ActivationFunction, Perceptron}
import neuroevolution.{Converter, neuralnetwork}

/**
 * Created by beenotung on 3/21/15.
 */
object Test extends App {
  override def main(args: Array[String]) {
    printf("test")


    val frame: List[Int] = List(1, 3, 3, 1)
    val perceptron: Perceptron = Perceptron.create(frame)
    val converter: Converter = new Converter(N_BIT_BIAS = 8, N_BIT_WEIGHT = 8, numberOfNodes = neuralnetwork.Perceptron.getNumberOfNodes(perceptron))
    val ga: GA = new GA(converter.numberOfBits, test_evalFitness_function)
    ga.start
    new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          ga.request

          if (ga.bestRawCode != null)
            printf("%s\n", ga.bestRawCode.toString)

          Thread.sleep(1)
        }
      }
    }).start
  }

  def test_evalFitness_function(rowCodes: Array[Boolean]): Double = {
    BitCounter.evalFitness(rowCodes)
  }

  class testFunction extends ActivationFunction {
    override def eval(value: Double): Double = {
      math.sin(value)
    }
  }

}
