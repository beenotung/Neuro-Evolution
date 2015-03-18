package neuroevolution.ga.core

import neuroevolution.ga.utils.DataTypes.BooleanNum

/**
 * Created by beenotung on 3/17/15.
 */
class Gene(BIT_SIZE: Int, A_MUTATION: Double = 0.1d, eval_function: (Array[BooleanNum]) => Double) {
  val bits: Array[BooleanNum] = new Array[BooleanNum](BIT_SIZE)
  var fitness: Double = 0D
  var diversity:Double =1D

  def eval = {
    fitness = eval_function(bits)
  }

  def evalDiversity(centroid:Array[Double])={
    diversity=0
    for(bit<-bits.indices)
        //diversity+=Math.abs(bits(x)-centroid(x))
    diversity+=Math.pow(bits(bit)-centroid(bit),2)
  }


}


