package nl.specialtree.core

import scala.io
/**
  * Created by razmaklat on 22-3-16.
  */
class Printer {
  def askInput():String = {
    scala.io.StdIn.readLine()
  }
}

object Main extends App{
  val a = new Printer
  a askInput()

}
