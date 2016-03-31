package nl.specialtree.core

import nl.specialtree.config.Config

/**
  * Created by jiar on 29-3-16.
  */
class Algorithms {
  def slopeOne(dataset:Map[String, UserPref], item1:Int, item2:Int, recursion:Boolean=true): (Double, Int) = {
    if(recursion) return slopeOneRecursive(dataset, item1, item2)
    var currDeviation:Double = 0
    var cardinality:Int = 0
    for(a <- dataset ){
      if(a._2.hasRated(item1) && a._2.hasRated(item2)){
        if(Config.debug) {println("===="); println(a._2.getRating(item1)); println(a._2.getRating(item2))}
        currDeviation += (a._2.getRating(item1) - a._2.getRating(item2))
        cardinality+=1
      }
    }
    (currDeviation/cardinality, cardinality)
  }
  private def slopeOneRecursive(dataset:Map[String, UserPref], item1:Int, item2:Int): (Double, Int) ={
    val result = getCurrentDeviation(dataset, item1, item2)
    (result._1/result._2, result._2)
  }
  private def getCurrentDeviation(dataset:Map[String, UserPref], item1:Int, item2:Int, cardinality:Int=0, value:Double=0, index:Int=0):(Double, Int) = {
    if(dataset.size-1 < index) return (value, cardinality)

    val datasetArray = dataset.toArray
    val a = datasetArray(index)
    if (!a._2.hasRated(item1) && a._2.hasRated(item2)) {
      getCurrentDeviation(dataset, item1, item2, cardinality=cardinality, value=value, index + 1)
    }else {
      val item1Rating: Double = a._2.getRating(item1)
      val item2Rating: Double = a._2.getRating(item2)
      if (Config.debug){println("===="); println(item1Rating); println(item2Rating)}
      getCurrentDeviation(dataset, item1, item2, cardinality=cardinality+1, value=value + (item1Rating - item2Rating), index + 1)
    }
  }
}
