import java.util

import neuroevolution.geneticalgorithm.{GA, ProblemType}

import scala.util.Random

object GABasicTest extends App {
  println("begin")
  var lastFitness = Random.nextDouble()
  var sameFitnessCount = 0
  val ga = new GA(
    POP_SIZE = 100
    , BIT_SIZE = 32
    , P_SELECTION = 0.25
    , P_MUTATION_POW = 1d
    , A_MUTATION_POW = 1d
    , PARENT_IMMUTABLE = true
    , EVAL_FITNESS_FUNCTION = gene => {
      gene.foldLeft(0)((acc, c) => if (c) acc + 1 else acc)
    }
    , PROBLEM_TYPE = ProblemType.Minimize
    , LOOP_INTERVAL = 10
    , diversityWeight = .5
  ) {
    override def loop: Unit = {
      super.loop
      val bestFitness = getBestFitness
      println("Best Fitness:", bestFitness)
      println("Best Gene:", util.Arrays.toString(getBestGene.rawCode))
      if (bestFitness == 0) {
        shouldLoop = false
      } else if (bestFitness == lastFitness) {
        sameFitnessCount += 1
        println("same fitness for", sameFitnessCount, "rounds")
        if (sameFitnessCount == 100) {
          println("reset genes")
          sameFitnessCount = 0
          setup
        }
      } else {
        lastFitness = bestFitness
        sameFitnessCount = 0
      }
    }
  }
  ga.start()
  println("end")
}