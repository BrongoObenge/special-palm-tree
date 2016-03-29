package nl.specialtree.core

/**
  * Created by jiar on 29-3-16.
  */
class Hoer {
  def calculateAllDeviations(dataset:Map[String, UserPref]):Map[Int, ItemReference] = {
    var returnVal:Map[Int, ItemReference] = Map()
    val alg = new Algorithms()
    val keys:List[Int] = this.getAllKeys(dataset)
    var tempKey1 = -1
    var tempKey2 = -1
    for(key1 <- keys)
      tempKey1 = key1
      for(key2 <- keys)
        tempKey2 = key2
        if(tempKey1 != tempKey2) {
          val result = alg.slopeOne(dataset, tempKey1, tempKey2)
          if(returnVal.contains(tempKey1)){
            //Get Itemref add vals
            returnVal.get(tempKey1).get.results = returnVal.get(tempKey1).get.results.::(tempKey1, result._1, result._2)
          }else{
            //create itemref
            val itemRef:ItemReference = new ItemReference(tempKey1)
            itemRef.results = itemRef.results.::(tempKey1, result._1, result._2)
            returnVal += (tempKey1 -> itemRef)
          }
        }
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
}
