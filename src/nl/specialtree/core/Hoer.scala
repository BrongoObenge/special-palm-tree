package nl.specialtree.core

import nl.specialtree.config.Config

/**
  * Created by jiar on 29-3-16.
  */

class Hoer {
  def calculateAllDeviations(dataset:Map[String, UserPref]):Map[Int, ItemReference] = {
    var returnVal: Map[Int, ItemReference] = Map[Int, ItemReference]()
    val alg = new Algorithms()
    val keys: List[Int] = this.getAllKeys(dataset, recursion = false)
    var tempKey1 = -1
    var tempKey2 = -1
    for (key1 <- keys) {
      tempKey1 = key1
      for (key2 <- keys) {
        tempKey2 = key2
        if (tempKey1 != tempKey2) {
          val result = alg.slopeOne(dataset, tempKey1, tempKey2, recursion = false)
          if (returnVal.contains(tempKey1)) {
            returnVal.get(tempKey1).get.results = returnVal.get(tempKey1).get.results.::(tempKey2, result._1, result._2)
          } else {
            val itemRef: ItemReference = new ItemReference(tempKey1)
            itemRef.results = itemRef.results.::(tempKey2, result._1, result._2)
            returnVal += (tempKey1 -> itemRef)
          }
        }
      }
    }
    if(Config.debug) this.printDeviationMatrix(returnVal)
  returnVal
  }

  def getAllKeys(dataset:Map[String, UserPref], recursion:Boolean=false):List[Int] = {
    if(recursion) getAllKeysRecursive(dataset, List(), 0) else getAllKeysNormal(dataset)
  }

  private def getAllKeysNormal(dataset:Map[String, UserPref]):List[Int] = {
    var keys:List[Int] = List()
    for(data <- dataset) {
      for(d <- data._2.ratings.iterator)
        if(!keys.contains(d._1))
          keys = keys.::(d._1)
    }
    keys
  }

  private def getAllKeysRecursive(dataset:Map[String, UserPref], list:List[Int]=List[Int](), index:Int=0):List[Int] = {
    val datasetArray = dataset.toArray
    if(index > datasetArray.length-1) return list
    val resultList:List[Int] = (matchKeys(datasetArray(index)._2) ++ list).distinct
    getAllKeysRecursive(dataset, list=resultList, index+1)
  }

  private def matchKeys(data:UserPref, list:List[Int]=List[Int](), index:Int=0):List[Int] = {
    val ratingsArray = data.ratings.toArray
    if(index > ratingsArray.length-1) return list
    if(!list.contains(ratingsArray(index)._1)){
      return matchKeys(data, ratingsArray(index)._1 :: list, index+1)
    }
    matchKeys(data, list, index+1)
  }

  def printDeviationMatrix(data:Map[Int, ItemReference]) = {
    println("=======DEVIATION MATRIX============")
    data.foreach{d => {println("==="); println("ID: " + d._1); d._2.results.foreach{x => println("[ID: "+x._1 + " Rating: "+ x._2 + " Cardinality: "+x._3+"]")}}}
    println("END=======DEVIATION MATRIX============")
  }

  def updateDevationMatrix(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double),    //TODO put in recursion
        item2:(Int, Double), recursive:Boolean=false):Map[Int, ItemReference] = {
    assert(item1._1 != item2._1)
    if(recursive) return a2(deviationMatrix, item1, item2)
    val itemReference:ItemReference = deviationMatrix.get(item1._1).get
    var item:(Int, Double, Int) = (0,0,0)
    for(a <- itemReference.results.iterator){
      if(a._1 == item2._1)
        item = a
    }
    if(item == (0,0,0))return deviationMatrix
    val newDeviation:Double = ((item._2*item._3)+(item1._2 - item2._2))/(item._3+1) //(CurrentDeviation * Cardinality)+(item1Rating - item2Rating)/Cardinality+1
    if(Config.debug) print("===Deviation Updated===\nOld deviation: "+ item._2+"\nNew deviation: "+ newDeviation+"\n===")
    deviationMatrix.get(item1._1).get.results = deviationMatrix.get(item1._1).get.results.filter(x => x == item)
    deviationMatrix.get(item1._1).get.results = deviationMatrix.get(item1._1).get.results.::(item._1, newDeviation, item._3+1)

    if(Config.debug) this.printDeviationMatrix(deviationMatrix)
    deviationMatrix
  }

  //=====ATTEMPT2
  private def a2(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double), item2:(Int, Double),
    index:Int=0):Map[Int, ItemReference]= {
    assert(item1._1 != item2._1)
    val item1ResultsArr:Array[(Int, Double, Int)] = deviationMatrix.get(item1._1).get.results.toArray
    if(index>item1ResultsArr.length-1) return deviationMatrix
    val item = item1ResultsArr(index)
    val newDeviation:Double = ((item._2*item._3)+(item1._2 - item2._2))/(item._3+1)
    println("===Deviation Updated===\nOld deviation: "+ item._2+"\nNew deviation: "+ newDeviation+"\n===")
    val newList = deviationMatrix.get(item1._1).get.results.filter(x => x == item).::(item._1, newDeviation, item._3+1)
    a2(rep(deviationMatrix, newList, item1._1), item1, item2, index+1)
  }
  private def rep(map:Map[Int, ItemReference], newVal:List[(Int, Double, Int)], index:Int):Map[Int, ItemReference]={
    map.filter((k) => k._1 != index) ++ Map[Int, ItemReference](index -> new ItemReference(index, newVal))
  }
  //=====ATTEMPT1 DOESN'T WORK
  private def updateDevationMatrixRecursive(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double), item2:(Int, Double),
                                            index:Int=0, result:Map[Int, ItemReference]= Map[Int, ItemReference]()):Map[Int, ItemReference] = {
    assert(item1._1 == item2._1)
    val itemReferenceArr:Array[(Int, Double, Int)] = deviationMatrix.get(item1._1).get.results.toArray
    if(index > itemReferenceArr.length-1) return result

    val a = itemReferenceArr(index)
    val newDeviation:Double = ((a._2*a._3)+(item1._2 - item2._2))/(a._3+1) //(CurrentDeviation * Cardinality)+(item1Rating - item2Rating)/Cardinality+1
    if(Config.debug) print("===Deviation Updated===\nOld deviation: "+ a._2+"\nNew deviation: "+ newDeviation+"\n===")
    val tempResult: Map[Int, ItemReference] = replaceItemInMap(deviationMatrix, new ItemReference(index, deviationMatrix.get(item1._1).get.results.filter(x => x == a).::(a._1, newDeviation, a._3+1)), index)
    updateDevationMatrixRecursive(deviationMatrix, item1, item2, index+1, tempResult)
  }

  private def replaceItemInMap(deviationMatrix:Map[Int, ItemReference], replacementObj:ItemReference, replaceIndex:Int, index:Int=0, result:Map[Int, ItemReference]=Map[Int, ItemReference]()):Map[Int, ItemReference] = {
    if(index>deviationMatrix.size-1) return result
    val arr = deviationMatrix.toArray
    if(index == replaceIndex) replaceItemInMap(deviationMatrix, replacementObj, replaceIndex, index+1, result ++ Map(arr(replaceIndex)._1 -> replacementObj))
    else replaceItemInMap(deviationMatrix, replacementObj, replaceIndex, index+1, result ++ Map(arr(index)._1 -> arr(index)._2))
  }
  //END =====ATTEMPT1 DOESN'T WORK

}
