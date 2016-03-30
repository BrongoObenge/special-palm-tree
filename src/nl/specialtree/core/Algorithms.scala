package nl.specialtree.core

import nl.specialtree.config.Config

/**
  * Created by jiar on 29-3-16.
  */
class Algorithms {
  def slopeOne(dataset:Map[String, UserPref], item1:Int, item2:Int, recursion:Boolean): (Double, Int) = {
    if(recursion) return slopeOneRecursive(dataset, item1, item2)
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
  def slopeOneRecursive(dataset:Map[String, UserPref], item1:Int, item2:Int): (Double, Int) ={
    val a = getCurrentDeviation(dataset, item1, item2, 0, dataset.size-1, 0, 0)
    (a._1/a._2, a._2)
  }
  def getCurrentDeviation(dataset:Map[String, UserPref], item1:Int, item2:Int, i:Int, max:Int, cardinality:Int, value:Double):(Double, Int) = {
    if(max < i) return (value, cardinality)

    val datasetArray = dataset.toArray
    println(i)
    val a = datasetArray(i)
    if (!a._2.hasRated(item1) && a._2.hasRated(item2)) {
      getCurrentDeviation(dataset, item1, item2, i + 1, max, cardinality, value)
    }else {
      val item1Rating: Double = a._2.getRating(item1)
      val item2Rating: Double = a._2.getRating(item2)
      if (Config.debug) {
        println("====")
        println(item1Rating)
        println(item2Rating)
      }
      getCurrentDeviation(dataset, item1, item2, i + 1, max, cardinality+1, value + (item1Rating - item2Rating))
    }
  }
}
