package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  val dataService = new LoadDataService()
  val userMap:Map[String, UserPref] = dataService loadSmallDataset()
  val h:Hoer = new Hoer()

  h.printDeviationMatrix(h.updateDevationMatrix(h.calculateAllDeviations(userMap), (101,1),(101,5), recursive = true))
  println("--------------------------------")
  h.printDeviationMatrix(h.updateDevationMatrix(h.calculateAllDeviations(userMap), (101,1),(101,5), recursive = false))

  //  println(h.getAllKeys(userMap))
//  println("abed.createApplication('F# Datascience opdracht')")
//  println("=====")
//  println(h.getAllKeys(userMap, recursion = true))
//
//  println(userMap.get("1").get.hasRated(101))
//  println("======")
}
