package nl.specialtree.core


/**
  * Created by jiar on 29-3-16.
  */

class UserPref(id:String) {
  val userId = id
  var ratings:List[(Int, Double)] = List()

  def getRating(item:Int, recursion:Boolean=true):Double = {
    if (recursion) return getRatingRecursive(item)
    for(r <- ratings){
      if(r._1 == item)
        return r._2
    }
    -1
  }

  def hasRated(item:Int):Boolean ={
    if(getRating(item, recursion=true) != -1) true else false
  }

  private def getRatingRecursive(item:Int, index:Int=0):Double = {
    val ratingsArr = ratings.toArray
    if(index>ratingsArr.size-1) return -1
    if(ratingsArr(index)._1 == item)
      return ratingsArr(index)._2
    getRatingRecursion(item, index+1)
  }
}
