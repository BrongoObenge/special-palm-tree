package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  val dataService = new LoadDataService()
  val userMap:Map[String, UserPref] = dataService loadSmallDataset()
  val h:Hoer = new Hoer()

//  println(h.getAllKeys(userMap))
//  println("=====")
//  println(h.getAllKeys(userMap, recursion = true))
//
//  println(userMap.get("1").get.hasRated(101))
  println("======")
}
