package nl.specialtree.core

import nl.specialtree.config.Config

import scala.io.Source
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  val debug = false
  var items:Map[String,UserPref] = Map()
  val source = Source.fromFile(Config.dataLocation)
  try {

    for(line <- source.getLines()) {
      val ln = line.split(',')
      if(debug) println(s"user ${ln(0)} item ${ln(1)} itemRating ${ln(2)}")
      if(items.contains(ln(0))){
        var x = items.get(ln(0)).get
        x.ratings = x.ratings.::(ln(1).toInt:Int, ln(2).toDouble:Double)
      }else{
        var a = new UserPref(ln(0))
        a.ratings = a.ratings.::(ln(1).toInt:Int, ln(2).toDouble:Double)
        items+=(ln(0) -> a)
      }
    }
  } finally {
    source.close()

    if(debug) {
      println("=========")
      for(a <- items)
        println(a._2.ratings)
    }
  }
}
