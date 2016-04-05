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

//  Recommendation SHIZA <non recursive>
/*    println("-------------------------------------------------------")
    println("Recommendation session")
    val recommendations = h.recommendations("186",userMap,h.calculateAllDeviations(userMap))
    recommendations.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
    println("-------------------------------------------------------")*/
//  END recommendation SHIZA

  //  Recommendation SHIZA <recursive>
    println("-------------------------------------------------------")
    println("Recommendation session")
    val recommendations = h.recommendationsRecursive("7",userMap,h.calculateAllDeviations(userMap), 1)
    recommendations.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
    println("-------------------------------------------------------")
  //  END recommendation SHIZA


  //PREDICTION
/*  println("user 7")
  println("Item 101 " + new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (7, 101)))
  println("Item 103 " + new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (7, 103)))
  println("Item 106 " + new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (7, 106)))
  println("User 3")
  println("Item 103 " + new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (3, 103)))
  println("Item 105 " + new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (3, 105)))*/

}
