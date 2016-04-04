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
    for(item <- dataset ){
      if(item._2.hasRated(item1) && item._2.hasRated(item2)){
        if(Config.debug) {println("===="); println(item._2.getRating(item1)); println(item._2.getRating(item2))}
        currDeviation += (item._2.getRating(item1) - item._2.getRating(item2))
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

  //Req._2 = (User, Item)
  //result = (numinator, denominator)
  def predictRating(userPreference:Map[String, UserPref], deviationMatrix:Map[Int, ItemReference], request:(Int, Int),
                            index:Int=0, result:(Double, Double)=(0,0)): Double = {
    val currentUserItemRef:List[(Int, Double, Int)] = deviationMatrix.get(request._2).get.results //DEVIATION MATRIX
    val currentUserUserPref:Array[(Int, Double)] = userPreference.get(request._1.toString).get.ratings.toArray  //RATINGS
    if(index>currentUserUserPref.length-1) return result._1/result._2

    val j = currentUserUserPref(index) // Get deviation from i to this
    val deviation:(Int, Double, Int) = matchSomeShit(currentUserItemRef, j._1)
    val jrating:Double = anotherBullshitMatcher(currentUserUserPref, j._1)

    predictRating(userPreference, deviationMatrix, request, index+1,
      (result._1 +((jrating + deviation._2) * deviation._3), result._2+deviation._3))
  }

  //TODO Discriminated Union?
  private def matchSomeShit(arr:List[(Int, Double, Int)], matchValue:Int, index:Int=0):(Int, Double, Int) = {  //TODO give a function to compare to
    if(index>arr.length-1) return (-1,-1,-1)
    if(arr(index)._1 == matchValue) return arr(index)
    matchSomeShit(arr, matchValue, index+1)
  }
  private def anotherBullshitMatcher(arr:Array[(Int, Double)], matchValue:Int, index:Int=0):Double = {  //TODO give a function to compare to
    if(index>arr.length-1) return -1
    if(arr(index)._1 == matchValue) return arr(index)._2
    anotherBullshitMatcher(arr, matchValue, index+1)
  }
}
