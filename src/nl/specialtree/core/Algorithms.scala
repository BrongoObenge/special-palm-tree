package nl.specialtree.core

import nl.specialtree.config.Config

/**
  * Created by jiar on 29-3-16.
  */
class Algorithms {
  def slopeOne(dataset:Map[String, UserPref], item1:Int, item2:Int) = {
    var currDeviation:Double = 0
    var cardinality:Int = 0
    for(a <- dataset ){
      if(a._2.hasRated(item1) && a._2.hasRated(item2)){
        if(Config.debug) {
          println("====")
          println(a._2.getRating(item1))
          println(a._2.getRating(item2))
        }
        currDeviation += (a._2.getRating(item1) - a._2.getRating(item2))
        cardinality+=1
      }
    }
    (currDeviation/cardinality, cardinality)
  }

  def getCurrentDeviation(dataset:Map[String, UserPref], i:Int, y:Int, value:Double): Double ={
    if (i > y) return value

    
    return 0

  }
}
