package nl.specialtree.core

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/**
  * Created by razmaklat on 17-4-16.
  */
class HandyToolFunctional {

  def calcDeviations(userMap:Map[Int,UserPref]):Map[Int,ItemReference] = {
    //set all data in place
    val deviationMatrix = setAllData(userMap)
    //compute the deviations
    val computedDeviation = computeDeviations(deviationMatrix)
    //return the deviation matrix
    //computedDeviation
    computedDeviation
  }

  private def setAllData(userMap:Map[Int,UserPref],deviationMatrix:Map[Int,ItemReference] = Map()):Map[Int,ItemReference] = {
    @tailrec
    def setAllDatRec(userMap:Seq[(Int,UserPref)],deviationMatrix:Map[Int,ItemReference],
          userIndex:Int,item1Index:Int,item2Index:Int):Map[Int,ItemReference] = {
      if(userIndex > userMap.size -1 ) return deviationMatrix
      val user = userMap(userIndex)
      if(item1Index > user._2.ratings.size - 1) {
        setAllDatRec(userMap,deviationMatrix,userIndex + 1,0,0)
      } else if(!deviationMatrix.contains(user._2.ratings(item1Index)._1)) {
        setAllDatRec(userMap,deviationMatrix.updated(user._2.ratings(item1Index)._1,new ItemReference(user._2.ratings(item1Index)._1)),userIndex,item1Index,item2Index)
      } else if(item2Index > user._2.ratings.size - 1){
        setAllDatRec(userMap,deviationMatrix,userIndex,item1Index + 1,0)
      } else {
        val item = user._2.ratings(item1Index)
        val item2 = user._2.ratings(item2Index)
        if(item._1 != item2._1) {
          val itemResults = deviationMatrix.get(item._1).get.results
          if(itemResults.exists{x => x._1 == item2._1}) {
            val itemInRatings = itemResults.find(x => x._1 == item2._1).get
            val dev = itemInRatings._2
            val freq = itemInRatings._3
            val newDev = dev + (item._2 - item2._2)
            val newFreq = freq + 1
            val newItemInRatings = (itemInRatings._1,newDev,newFreq)
            val newResults = itemResults.filterNot(x => x._1 == item2._1).::(newItemInRatings)
            val updatedItemRef:ItemReference = new ItemReference(item._1,newResults)
            setAllDatRec(userMap,deviationMatrix.updated(item._1,updatedItemRef),userIndex,item1Index,item2Index + 1)
          } else {
            val dev = item._2 - item2._2
            val freq = 1
            val newResults = itemResults.::((item2._1,dev,freq))
            val updatedItemRef:ItemReference = new ItemReference(item._1,newResults)
            setAllDatRec(userMap,deviationMatrix.updated(item._1,updatedItemRef),userIndex,item1Index,item2Index + 1)
          }
        } else {
          setAllDatRec(userMap,deviationMatrix,userIndex,item1Index,item2Index + 1)
        }
      }
    }
    setAllDatRec(userMap.toSeq,deviationMatrix,0,0,0)
  }

  private def computeDeviations(deviationMatrix:Map[Int,ItemReference]) : Map[Int,ItemReference] = {
    @tailrec
    def calcDeviations(devMatrix:Seq[(Int,ItemReference)], item1Index:Int,
                       item2Index:Int, deviationMatrix:Map[Int,ItemReference]): Map[Int,ItemReference] = {
      if(item1Index > devMatrix.size -1) {
         deviationMatrix
      } else {
        if(item2Index > devMatrix(item1Index)._2.results.size - 1) {
          calcDeviations(devMatrix,item1Index +1,0,deviationMatrix)
        } else {
          val item1 = devMatrix(item1Index)
          val item2 = item1._2.results(item2Index)
          val item1Results = devMatrix(item1Index)._2.results
          val sumOfDeviation = item2._2
          val freq = item2._3
          val deviation = sumOfDeviation / freq
          val newItemInRatings = (item2._1,deviation,freq)
          val newResults = item1Results.filterNot(x => x._1 == item2._1).::(newItemInRatings)
          val updatedItemRef:ItemReference = new ItemReference(item1._1,newResults)
          val updatedDeviationMatrix = deviationMatrix.updated(item1._1,updatedItemRef)
          calcDeviations(updatedDeviationMatrix.toSeq,item1Index,item2Index + 1,updatedDeviationMatrix)
        }
      }
    }
    calcDeviations(deviationMatrix.toSeq,0,0,deviationMatrix)
  }

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
    @tailrec
    def updateDeviationMatrixRec(userMap:Map[Int,UserPref], deviationMatrix:Map[Int, ItemReference],
          userItemRating:(Int,Int, Double),userIndex:Int):Map[Int, ItemReference] = {
      val userPreference:UserPref = userMap.get(userItemRating._1).get
      val item1 = (userItemRating._2,userItemRating._3)
      if(userIndex > userPreference.ratings.size - 1) return deviationMatrix
      val itemInResults = userPreference.ratings(userIndex)
      if(itemInResults._1 != userItemRating._2) {
        val item2 = (itemInResults._1,itemInResults._2)
        val devMatrixVer = updateDeviation(deviationMatrix,item1,item2)
        val devMatrixHor = updateDeviation(devMatrixVer,item2,item1)
        // devMatrixHor is the completely updated deviation matrix.
        updateDeviationMatrixRec(userMap,devMatrixHor,userItemRating,userIndex + 1)
      } else {
        updateDeviationMatrixRec(userMap,deviationMatrix,userItemRating,userIndex + 1)
      }
    }
    updateDeviationMatrixRec(userMap,deviationMatrix,userItemRating,0)
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
