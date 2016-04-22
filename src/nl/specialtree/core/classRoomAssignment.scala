package nl.specialtree.core

import scala.annotation.tailrec

/**
  * Created by razmaklat on 22-4-16.
  */
class classRoomAssignment {
  //Assignment 1
  def fizzBuzz(p1:Int,p2:Int):String = {
    if(p1 % 4 == 0 ) {
      if(p2 % 4 == 0) {
        "FizzBuzz"
      } else {
        "Fizz"
      }
    }else if(p2 % 4 == 0) {
       "Buzz"
    } else {
      "nothing"
    }
  }

  //Assignment 2
  def mrZipper(listA:Array[Any],listB:Array[Any]) :Map[Any,Any] = {
    @tailrec
    def zipping(listA:Array[Any],listB:Array[Any],index:Int,zippedMap:Map[Any,Any]) :Map[Any,Any] = {
      if(index > listA.length - 1) {
        zippedMap
      } else if(index > listB.length - 1) {
        zippedMap
      } else {
        zipping(listA,listB,index + 1,zippedMap.updated(listA(index),listB(index)))
      }
    }
    zipping(listA,listB,0,zippedMap = Map())
  }

  def mrUnZipper(zippedMap:Map[Any,Any]):(Seq[Any],Seq[Any]) = {
    def unZipper(zippedMap:Seq[(Any,Any)],zippedTuple:(Seq[Any],Seq[Any]),index:Int)
        :(Seq[Any],Seq[Any]) = {
      if(index > zippedMap.size -1) {
         zippedTuple
      } else {
        val zippedMapItem = zippedMap(index)
        val sLeft:Seq[Any] = zippedTuple._1.:+(zippedMapItem._1)
        val sRight:Seq[Any] = zippedTuple._2.:+(zippedMapItem._2)
        val newZippedTuple = (sLeft,sRight)
        unZipper(zippedMap,newZippedTuple,index + 1)
      }

    }
    unZipper(zippedMap.toSeq,(Seq(),Seq()),index = 0)
  }

}
