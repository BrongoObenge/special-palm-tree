package nl.specialtree.core

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/**
  * Created by razmaklat on 17-4-16.
  */
class HandyToolFunctional {
  private def updateDeviation(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double),
                              item2:(Int, Double)):Map[Int, ItemReference] = {
    val itemRef:ItemReference = deviationMatrix.get(item1._1).get
    @tailrec
    def updateDev(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double),
                  item2:(Int, Double),results:List[(Int, Double, Int)],
                  currentItem:Int = 0):Map[Int, ItemReference] = {
      if(currentItem > results.length - 1 ) return deviationMatrix
      val itemRef:ItemReference = deviationMatrix.get(item1._1).get
      val itemInResults = results(currentItem)
      if(itemInResults._1 == item2._1) {
        val newDeviation:Double = ((itemInResults._2*itemInResults._3)+(item1._2 - item2._2))/(itemInResults._3+1) //(CurrentDeviation * Cardinality)+(item1Rating - item2Rating)/Cardinality+1
        val newItemInResults = (itemInResults._1,newDeviation,itemInResults._3+1)
        val newResults = itemRef.results.filterNot(x => {x == itemInResults}).::(newItemInResults)
        val newItemRef:ItemReference = new ItemReference(item1._1,newResults)
        updateDev(deviationMatrix.updated(item1._1,newItemRef),item1,item2,results,currentItem + 1)
      } else {
        updateDev(deviationMatrix,item1,item2,results,currentItem + 1)
      }
    }
    updateDev(deviationMatrix,item1,item2,itemRef.results,0)
  }

  def updateDeviationMatrix(userMap:Map[Int,UserPref], deviationMatrix:Map[Int, ItemReference],
                            userItemRating:(Int,Int, Double)):Map[Int, ItemReference] = {
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

  def predictRating(userPreference:UserPref, deviationMatrix:Map[Int, ItemReference],
                       itemToPredict:Int) : Double = {
    val userRatedItemList:List[(Int,Double)] = userPreference.ratings
    @tailrec
    def predictRatingAccumulator(deviationMatrix:Map[Int, ItemReference],
                                 itemToPredict:Int,numerator:Double,denominator:Double,
                                 userRatedItemList:List[(Int,Double)]
                                 ,currentItem:Int = 0) : Double = {
      if(currentItem > userRatedItemList.length - 1) return numerator / denominator
      val item = userRatedItemList(currentItem)
      val deviation = deviationMatrix.get(itemToPredict).get.results.find{x => x._1 == item._1}.getOrElse((0,0.0,0))
      if(deviation._1 != 0 && deviation._2 != 0) {
        predictRatingAccumulator(deviationMatrix,itemToPredict,numerator + (item._2 + deviation._2) * deviation._3,
          denominator + deviation._3,userRatedItemList,currentItem +1)
      } else {
        predictRatingAccumulator(deviationMatrix,itemToPredict,numerator,
          denominator,userRatedItemList,currentItem +1)
      }
    }
    predictRatingAccumulator(deviationMatrix,itemToPredict,0.0,
      0.0,userRatedItemList)
  }

  def recommendations(user:Int, userDataSet:Map[Int,UserPref], deviationMatrix:Map[Int,ItemReference], limit:Int=0)
  : Map[Int,Double] = {
    val devMatrix = deviationMatrix.toSeq
    @tailrec
    def recommendationsAccumulator(user:Int, userDataSet:Map[Int,UserPref],
                                   deviationMatrix:Map[Int,ItemReference],
                                   currentItem:Int,recommendations:Map[Int,Double],
                                   devMatrix:Seq[(Int,ItemReference)],limit:Int)
    : Map[Int,Double] = {
      if(currentItem > deviationMatrix.size - 1) {
        val sortedRecommendations = ListMap(recommendations.toSeq.sortWith(_._2 > _._2):_*).take(limit)
        return sortedRecommendations
      }
      val userItems:Array[(Int, Double)] = userDataSet.get(user).get.ratings.toArray
      //val devMatrix = deviationMatrix.toSeq
      val otherItem = devMatrix(currentItem)
      if(!userItems.exists{a => a._1 == otherItem._1} ) {
        val itemID = otherItem._1
        val predictedRating = predictRating(userDataSet.get(user).get,deviationMatrix,itemID)
        recommendationsAccumulator(user,userDataSet,deviationMatrix,currentItem + 1,
          recommendations.updated(itemID,predictedRating),devMatrix,limit)
      } else {
        recommendationsAccumulator(user,userDataSet,deviationMatrix,currentItem + 1,
          recommendations,devMatrix,limit)
      }
    }
    recommendationsAccumulator(user,userDataSet,deviationMatrix,0,Map[Int,Double](),devMatrix,limit)
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
