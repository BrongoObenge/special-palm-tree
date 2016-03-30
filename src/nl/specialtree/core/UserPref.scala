package nl.specialtree.core


/**
  * Created by jiar on 29-3-16.
  */

class UserPref(id:String) {
  val userId = id
  var ratings:List[(Int, Double)] = List()

  def getRating(item:Int):Double = {
    for(r <- ratings){
      if(r._1 == item)
        return r._2
    }
    -1
  }
  def hasRated(item:Int):Boolean ={
    for(r <- ratings){
      if(r._1 == item)
        return true
    }
    false
  }

  def hasRatedRecursion(item:Int, index:Int):Boolean = {
    val ratingsArray = ratings.toArray
    if(index>ratingsArray.size-1) return false
    //if(ratingsArra
  }
}
