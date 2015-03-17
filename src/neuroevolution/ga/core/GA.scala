package neuroevolution.ga.core

/**
 * Created by beenotung on 1/30/15.
 */
class GA(POP_SIZE: Int = 32, BIT_SIZE: Int, @deprecated MATURE_ROUND: Int = 40,eval_function:(Array[Boolean])=>Double) extends Thread {
  val genes: Array[Gene] = new Array[Gene](POP_SIZE)

  def setup: Unit = {
    for (i <- genes.indices) {
      genes(i) = new Gene(BIT_SIZE,eval_function)
    }
  }

  def select={}
  def crossover={}
  def eval={
    for(gene<-genes)
      gene.eval
  }

  def loop: Unit = {
    eval
    select
    crossover
  }
}
