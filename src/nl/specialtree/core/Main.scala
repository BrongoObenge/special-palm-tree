package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  val userMap:Map[String, UserPref] = new LoadDataService() loadSmallDataset()
  val h:ReallyHandyToolsMustUseThisClassForBestResults = new ReallyHandyToolsMustUseThisClassForBestResults()

  h.getUserWithNonRatedItems(userMap)
//  h.printDeviationMatrix(h.calculateAllDeviations(userMap))
//  println("1--------------------------------")

//  println(new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (4, 101)))
//  println("2--------------------------------")
//  h.printDeviationMatrix(h.updateDevationMatrix(h.calculateAllDeviations(userMap), (101,1),(101,5), recursive = true))

  //  println(h.getAllKeys(userMap))
//  println("abed.createApplication('F# Datascience opdracht')")
//  println("=====")
//  println(h.getAllKeys(userMap, recursion = true))
//
//  println(userMap.get("1").get.hasRated(101))
//  println("======")

//  Recommendation SHIZA
    println("-------------------------------------------------------")
    println("Recommendation session")
    val recommendations = h.recommendations("6",userMap,h.calculateAllDeviations(userMap))
    recommendations.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
    println("-------------------------------------------------------")
}
