package nl.specialtree.core

/**
  * Created by jiar on 29-3-16.
  */
class ItemReference(newId:Int, newResult:List[(Int,Double,Int)]=List[(Int, Double, Int)]()) {
  val id:Int = newId
  var results:List[(Int, Double, Int)] = newResult
}
