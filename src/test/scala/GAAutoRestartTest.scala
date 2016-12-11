import neuroevolution.geneticalgorithm.{GA, ProblemType}

object GAAutoRestartTest extends App {
  val ga = new GA(
    100
    , 32
    , 0.25
    , 1
    , 1
    , true
    , gene => {
      gene.count(identity)
    }
    , ProblemType.Minimize
  ) {
    override def loop: Unit = {
      super.loop
      val bestFitness = getBestFitness
      println(round, bestFitness)
      if (bestFitness == 0)
        shouldLoop = false
    }
  }
  ga.runAutoRestart(100)
}