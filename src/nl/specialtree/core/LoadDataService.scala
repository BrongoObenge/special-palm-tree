package nl.specialtree.core

import nl.specialtree.config.Config

import scala.io.Source

/**
  * Created by jiar on 29-3-16.
  */
class LoadDataService(debug:Boolean) {
  var userMap:Map[String, UserPref] = Map()
  val source = Source.fromFile(Config.dataLocation)
  def loadSmallDataset():Map[String, UserPref] = {
    try {
      for (line <- source.getLines()) {
        val ln = line.split(',')
        if (debug) println(s"user ${ln(0)} item ${ln(1)} itemRating ${ln(2)}")
        if (userMap.contains(ln(0))) {
          val tempUserPreference = userMap.get(ln(0)).get
          tempUserPreference.ratings = tempUserPreference.ratings.::(ln(1).toInt: Int, ln(2).toDouble: Double)
        } else {
          val tempUserPreference = new UserPref(ln(0))
          tempUserPreference.ratings = tempUserPreference.ratings.::(ln(1).toInt: Int, ln(2).toDouble: Double)
          userMap += (ln(0) -> tempUserPreference)
        }
      }
    } finally {
      source.close()
      if (debug) {
        println("=========")
        for (a <- userMap)
          println(a._2.ratings)
      }
    }
    userMap
  }
}
