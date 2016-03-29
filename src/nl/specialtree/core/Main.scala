package nl.specialtree.core

import nl.specialtree.config.Config

import scala.io
import scala.io.Source
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  var items:Map[String,Map[Int,Double]] = Map()
  val source = Source.fromFile(Config.dataLocation)
  try {
    for(line <- source.getLines()) {
      val ln = line.split(',')
      println(s"user ${ln(0)} item ${ln(1)} itemRating ${ln(2)}")
    }
  } finally {
    source.close()
  }


}
