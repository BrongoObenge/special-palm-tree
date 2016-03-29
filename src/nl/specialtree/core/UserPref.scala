package nl.specialtree.core

import org.apache.commons.codec.language.MatchRatingApproachEncoder

/**
  * Created by jiar on 29-3-16.
  */
class UserPref(id:String) {
  val userId = id
  var ratings:List[Rating] = ()

  def addRating(itemId:Int, rating:Double): Unit ={
    ratings.::(new Rating(itemId, rating))
  }
}

class Rating(anItemId:Int, aRating:Double) {
  var itemId = anItemId
  var rating = aRating
}