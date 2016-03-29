package nl.specialtree.core

import nl.specialtree.config.Config

/**
  * Created by jiar on 29-3-16.
  */
class Hoer {
  def calculateAllDeviations(dataset:Map[String, UserPref]):Map[Int, ItemReference] = {
    var returnVal: Map[Int, ItemReference] = Map()
    val alg = new Algorithms()
    val keys: List[Int] = this.getAllKeys(dataset)
    var tempKey1 = -1
    var tempKey2 = -1
    for (key1 <- keys) {
      tempKey1 = key1
      for (key2 <- keys) {
        tempKey2 = key2
        if (tempKey1 != tempKey2) {
          val result = alg.slopeOne(dataset, tempKey1, tempKey2)
          if (returnVal.contains(tempKey1)) {
            //Get Itemref add vals
            returnVal.get(tempKey1).get.results = returnVal.get(tempKey1).get.results.::(tempKey2, result._1, result._2)
          } else {
            //create itemref
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

  def getAllKeys(dataset:Map[String, UserPref]):List[Int] = {
    var keys:List[Int] = List()
    for(data <- dataset) {
      for(d <- data._2.ratings.iterator)
        if(!keys.contains(d._1))
          keys = keys.::(d._1)
     }
    keys
  }

  def printDeviationMatrix(data:Map[Int, ItemReference]) = {
    println("=======DEVIATION MATRIX============")
    for(d <- data){
      println("===")
      println(d._1)
      d._2.results.foreach {println}
    }
    println("END=======DEVIATION MATRIX============")
  }

  def updateDevationMatrix(deviationMatrix:Map[Int, ItemReference], item1:(Int, Double),
        item2:(Int, Double)):Map[Int, ItemReference] = {
    assert(item1._1 == item2._1)

    val itemReference:ItemReference = deviationMatrix.get(item1._1).get
    for(item <- itemReference.results.iterator){
        val newDeviation:Double = ((item._2*item._3)+(item1._2 - item2._2))/(item._3+1) //(CurrentDeviation * Cardinality)+(item1Rating - item2Rating)/Cardinality+1
        print("===Deviation Updated===\nOld deviation: "+ item._2+"\nNew deviation: "+ newDeviation+"\n===")
        deviationMatrix.get(item1._1).get.results = deviationMatrix.get(item1._1).get.results.filter(x => x == item)
        deviationMatrix.get(item1._1).get.results = deviationMatrix.get(item1._1).get.results.::(item._1, newDeviation, item._3+1)

    }
    if(Config.debug) this.printDeviationMatrix(deviationMatrix)
    deviationMatrix
  }

}
