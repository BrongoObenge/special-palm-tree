package nl.specialtree.core

import nl.specialtree.config.Config

import scala.collection.immutable.ListMap

/**
  * Created by jiar on 29-3-16.
  */

class ReallyHandyToolsMustUseThisClassForBestResults {

  def newCalculateAllDeviations(dataset:Map[Int,UserPref]):Map[Int,ItemReference] = {
    var deviationMatrix:Map[Int,ItemReference] = Map()
    //adds all the dataset needed for the computation
    var count = 0
    for(user <- dataset) {
      for(item <- user._2.ratings) {
        if(!deviationMatrix.contains(item._1)){
          deviationMatrix += (item._1 -> new ItemReference(item._1))
        }
        for(item2 <- user._2.ratings) {
          if(item._1 != item2._1) {
            val itemResults = deviationMatrix.get(item._1).get.results
            if(itemResults.exists{x => x._1 == item2._1}) {
              //println("update1")
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
              //println("update2")
              val dev = item._2 - item2._2
              val freq = 1
              val newResults = itemResults.::((item2._1,dev,freq))
              val updatedItemRef:ItemReference = new ItemReference(item._1,newResults)
              deviationMatrix = deviationMatrix.updated(item._1, updatedItemRef)
            }
          }
        }
      }
    println(s"done, count: $count")
      count+=1
    }
    println("Beginning with deviation computation")
    //compute the deviation in matrix
    var newDevMatrix = deviationMatrix
    for(itemInResults <- deviationMatrix) {
      //val itemResults = deviationMatrix.get(itemInResults._1).get.results
      for(item2InResults <- deviationMatrix.get(itemInResults._1).get.results) {
        val itemResults = deviationMatrix.get(itemInResults._1).get.results
        val sumOfdeviation = item2InResults._2
        val /**/freq = item2InResults._3
        val deviation = sumOfdeviation / freq
        //println(s"item1 ${itemInResults._1} item2 ${item2InResults._1}")
        //println(s"sumOfDeviation $sumOfdeviation freq $freq deviation $deviation")
        val newItemInRatings = (item2InResults._1,deviation,freq)
        val newResults = itemResults.filterNot(x => x._1 == item2InResults._1).::(newItemInRatings)
        val updatedItemRef:ItemReference = new ItemReference(itemInResults._1,newResults)
        deviationMatrix = deviationMatrix.updated(itemInResults._1,updatedItemRef)
        //println("just for fun")
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

  def newRecommendations(user:Int, userDataSet:Map[Int,UserPref], deviationMatrix:Map[Int,ItemReference], limit:Int=0,
                      recursion:Boolean=false)
  : Map[Int,Double] = {
    //using the slope one algorithm
    if(recursion)return recommendationsRecursive(user,userDataSet,deviationMatrix, limit)
    var recommendations:Map[Int,Double] = Map[Int,Double]()
    val alg:Algorithms = new Algorithms()
    val userItems:Array[(Int, Double)] = userDataSet.get(user).get.ratings.toArray
    var recommendCounter = 0
    for(otherItem <- deviationMatrix) {
      if(!userItems.exists{a => a._1 == otherItem._1} ) {
        val itemID = otherItem._1
        val predictedRating = alg.newPredictRating(userDataSet.get(user).get,deviationMatrix,itemID)
        recommendations += (itemID -> predictedRating)
      }
/*      println(s"$recommendCounter recommendation done")
      recommendCounter += 1*/
    }
    val sortedRecommendations = ListMap(recommendations.toSeq.sortWith(_._2 > _._2):_*).take(limit)
    sortedRecommendations
  }

  //=====ATTEMPT1 recommendations Recursive
  def recommendationsRecursive(userID:Int,userDataSet:Map[Int,UserPref],deviationMatrix:Map[Int,ItemReference],
                               limit:Int=0):Map[Int,Double] = {
    val recommendations = traverseUserItems(userID,userDataSet,deviationMatrix)
    if(limit == 0 )ListMap(recommendations.toSeq.sortWith(_._2 > _._2):_*)
    else ListMap(recommendations.toSeq.sortWith(_._2 > _._2):_*).take(limit)
  }

  private def traverseUserItems(userID:Int,userDataSet:Map[Int,UserPref],deviationMatrix:Map[Int,ItemReference],
                                index:Int = 0,recommendation:Map[Int,Double] = Map[Int,Double]()) : Map[Int,Double] = {
    val userDataSetArr = userDataSet.get(userID).get.ratings.toArray
    if(index > userDataSetArr.length -1 ) return recommendation
    val recommendationMap = recommendation ++ traverseDeviationMatrix(userID.toInt,userDataSetArr(index)._1,userDataSet,deviationMatrix)
    traverseUserItems(userID,userDataSet,deviationMatrix,index+1,recommendation=recommendationMap)

  }

  private def traverseDeviationMatrix(userID:Int,itemID:Int,userDataSet:Map[Int,UserPref],
                                      deviationMatrix:Map[Int,ItemReference], index:Int = 0,
                                      recommendation:Map[Int,Double]=Map[Int,Double]()) : Map[Int,Double] = {
    if(index > deviationMatrix.size -1 ) return recommendation
    val alg = new Algorithms()
    val devMatrixArr = deviationMatrix.toArray
    val otherItem = devMatrixArr(index)
    val devMatrixResults:Array[(Int,Double,Int)] = deviationMatrix.get(otherItem._1).get.results.toArray
    val userItems:Array[(Int, Double)] = userDataSet.get(userID).get.ratings.toArray

    if(devMatrixResults.exists{a => a._1 == itemID} && !userItems.exists{a => a._1 == otherItem._1}) {
      val predictedRating = alg.predictRating(userDataSet,deviationMatrix,(userID,otherItem._1))
      val newMap = Map(otherItem._1 -> predictedRating) ++ recommendation
      traverseDeviationMatrix(userID,itemID, userDataSet, deviationMatrix, index+1, newMap)
    }else {
      traverseDeviationMatrix(userID, itemID, userDataSet, deviationMatrix, index + 1, recommendation)
    }
  }
  //END =====ATTEMPT1

  //a function found on stackoverflow to measure the execution time in nanoseconds
  //to which it will be converted in ms.
  def time[R](block: => R): R = {
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
