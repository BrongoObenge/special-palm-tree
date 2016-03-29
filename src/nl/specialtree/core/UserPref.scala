package nl.specialtree.core


/**
  * Created by jiar on 29-3-16.
  */

class UserPref(id:String) {
  val userId = id
  var ratings:List[(Int, Double)] = List()
}
