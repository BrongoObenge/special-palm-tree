package nl.specialtree.core

import nl.specialtree.config.Config

import scala.collection.immutable.ListMap

/**
  * Created by jiar and Razmaklat :P on 29-3-16.
  */

class ReallyHandyToolsMustUseThisClassForBestResults {

  def newCalculateAllDeviations(dataset:Map[Int,UserPref]):Map[Int,ItemReference] = {
    var deviationMatrix:Map[Int,ItemReference] = Map()
    //adds all the dataset needed for the computation
    for(user <- dataset) {
      for(item <- user._2.ratings) {
        if(!deviationMatrix.contains(item._1)){
          deviationMatrix += (item._1 -> new ItemReference(item._1))
        }
        for(item2 <- user._2.ratings) {
          if(item._1 != item2._1) {
            val itemResults = deviationMatrix.get(item._1).get.results
            if(itemResults.exists{x => x._1 == item2._1}) {
              val itemInRatings = itemResults.find(x => x._1 == item2._1).get
              var dev = itemInRatings._2
              var freq = itemInRatings._3
              dev += item._2 - item2._2
              freq += 1
              val newItemInRatings = (itemInRatings._1,dev,freq)
              val newResults = itemResults.filterNot(x => x._1 == item2._1).::(newItemInRatings)
              val updatedItemRef:ItemReference = new ItemReference(item._1,newResults)
              deviationMatrix = deviationMatrix.updated(item._1,updatedItemRef)
            } else {
              val dev = item._2 - item2._2
              val freq = 1
              val newResults = itemResults.::((item2._1,dev,freq))
              val updatedItemRef:ItemReference = new ItemReference(item._1,newResults)
              deviationMatrix = deviationMatrix.updated(item._1, updatedItemRef)
            }
          }
        }
      }
    }
    println("Beginning with deviation computation")
    //compute the deviation in matrix
    for(itemInResults <- deviationMatrix) {
      for(item2InResults <- deviationMatrix.get(itemInResults._1).get.results) {
        val itemResults = deviationMatrix.get(itemInResults._1).get.results
        val sumOfDeviation = item2InResults._2
        val freq = item2InResults._3
        val deviation = sumOfDeviation / freq
        val newItemInRatings = (item2InResults._1,deviation,freq)
        val newResults = itemResults.filterNot(x => x._1 == item2InResults._1).::(newItemInRatings)
        val updatedItemRef:ItemReference = new ItemReference(itemInResults._1,newResults)
        deviationMatrix = deviationMatrix.updated(itemInResults._1,updatedItemRef)
      }
    }
    deviationMatrix
  }

  def printDeviationMatrix(data:Map[Int, ItemReference]) = {
    println("=======DEVIATION MATRIX============")
    data.foreach{d => {println("==="); println("ID: " + d._1); d._2.results.foreach{x => println("[ID: "+x._1 + " Rating: "+ x._2 + " Cardinality: "+x._3+"]")}}}
    println("END=======DEVIATION MATRIX============")
  }

  private def updateDeviation(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double),
                           item2:(Int, Double)):Map[Int, ItemReference] = {
    val itemRef:ItemReference = deviationMatrix.get(item1._1).get
    for(itemInResults <- itemRef.results) {
      if(itemInResults._1 == item2._1) {
        val newDeviation:Double = ((itemInResults._2*itemInResults._3)+(item1._2 - item2._2))/(itemInResults._3+1) //(CurrentDeviation * Cardinality)+(item1Rating - item2Rating)/Cardinality+1
        val newItemInResults = (itemInResults._1,newDeviation,itemInResults._3+1)
        val newResults = itemRef.results.filterNot(x => {x == itemInResults}).::(newItemInResults)
        val newItemRef:ItemReference = new ItemReference(item1._1,newResults)
        return deviationMatrix.updated(item1._1,newItemRef)
      }
    }
    deviationMatrix
  }

  def updateDeviationMatrix(userMap:Map[Int,UserPref], deviationMatrix:Map[Int, ItemReference], userItemRating:(Int,Int, Double)):Map[Int, ItemReference] = {
    val userPreference:UserPref = userMap.get(userItemRating._1).get
    val item1 = (userItemRating._2,userItemRating._3)
    var newDevMatrix:Map[Int,ItemReference] = deviationMatrix
    for(itemInResults <- userPreference.ratings.iterator) {
      if(itemInResults._1 != userItemRating._2) {
        val item2 = (itemInResults._1,itemInResults._2)
        newDevMatrix = updateDeviation(newDevMatrix,item1,item2)
        newDevMatrix = updateDeviation(newDevMatrix,item2,item1)
      }
    }
    newDevMatrix
  }

  def addNewItemToUser(user:Int, item:Int, rating:Double, dataset:Map[Int, UserPref]):Map[Int, UserPref] = {
    //Add new Item and rating to the givenUser
    if(dataset.contains(user)) {
      val userPreference:UserPref = dataset.get(user).get
      if(!userPreference.ratings.contains((item,rating))) {
        val newUserPreference:UserPref = new UserPref(userPreference.userId,userPreference.ratings.::(item,rating))
        val newDataSet = dataset + (user -> newUserPreference)
        return newDataSet
      }
    }
    //return the normal dataset if the user does not exist in the dataset
    dataset
  }

  def recommendations(user:Int, userDataSet:Map[Int,UserPref], deviationMatrix:Map[Int,ItemReference], limit:Int=0)
  : Map[Int,Double] = {
    //using the slope one algorithm
    var recommendations:Map[Int,Double] = Map[Int,Double]()
    val alg:Algorithms = new Algorithms()
    val userItems:Array[(Int, Double)] = userDataSet.get(user).get.ratings.toArray
    for(otherItem <- deviationMatrix) {
      if(!userItems.exists{a => a._1 == otherItem._1} ) {
        val itemID = otherItem._1
        val predictedRating = alg.newPredictRating(userDataSet.get(user).get,deviationMatrix,itemID)
        recommendations += (itemID -> predictedRating)
      }
    }
    val sortedRecommendations = ListMap(recommendations.toSeq.sortWith(_._2 > _._2):_*).take(limit)
    sortedRecommendations
  }

  def time[R](block: => R): R = {
    //a function found on stackoverflow to measure the execution time in nanoseconds
    //to which it will be converted in ms.
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    //1000000ns is 1ms
    val timeInNs = t1 - t0
    println("Elapsed time: " + timeInNs + "ns")
    println("Elapsed time: " + timeInNs / 1000000 + "ms")
    result
  }
}
