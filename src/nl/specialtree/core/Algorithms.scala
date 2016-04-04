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
  //Req._1 = User
  //Req._2 = Item
  //result = (numinator, denominator)
  def predictRating(userPreference:Map[String, UserPref], deviationMatrix:Map[Int, ItemReference], request:(Int, Int),
                            index:Int=0, result:(Double, Double)=(0,0)): Double = {
    val currentUserItemRef:List[(Int, Double, Int)] = deviationMatrix.get(request._2).get.results //DEVIATION MATRIX
    val currentUserUserPref:Array[(Int, Double)] = userPreference.get(request._1.toString).get.ratings.toArray  //RATINGS
    if(index>currentUserUserPref.length-1) return result._1/result._2


    val i = request._2
    val j = currentUserUserPref(index)
    val jId = currentUserUserPref(index)._1 // Get deviation from i to this

    var devij:(Int, Double, Int) = (0,0,0)
    for(x <- currentUserItemRef){ //TODO recursive function
      if(x._1 == jId) devij = x
    }

    var jrating:Double = 0
    for(y <- currentUserUserPref){ //TODO recursive function
      if(y._1 == jId) jrating = y._2
    }

    predictRating(userPreference, deviationMatrix, request, index+1,
      (result._1 +((jrating + devij._2) * devij._3), result._2+devij._3))

    //      predictRating(deviationMatrix, request, index+1, ,cardinality+1)

  }
}
