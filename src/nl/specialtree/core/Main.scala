package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  //Import user and compute the deviation matrix
  val userMap:Map[Int, UserPref] = new LoadDataService() loadSmallDataset()
  val h:ReallyHandyToolsMustUseThisClassForBestResults = new ReallyHandyToolsMustUseThisClassForBestResults()
  val deviationMatrix:Map[Int,ItemReference] = h.calculateAllDeviations(userMap)
/*  val updatedDeviationMatrix:Map[Int,ItemReference] = h.updateDeviationMatrix(userMap,deviationMatrix,(3,105,4.0))
  println("==================================prediction user3==================================================")
  println("item: 103 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 103)))
  println("item: 105 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 105)))
  println("==============================END prediction user3==================================================")
  println(" ")
  println("==================================prediction user7==================================================")
  println("item: 101 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 101)))
  println("item: 103 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 103)))
  println("item: 106 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 106)))
  println("==============================END prediction user7==================================================")
  println(" ")
  println("==========================User 3 Updates Item 105 with 4.0 =========================================")
  println("==========================Updated prediction user7==================================================")
  println("item: 101 " + new Algorithms().predictRating(userMap, updatedDeviationMatrix, (7, 101)))
  println("item: 103 " + new Algorithms().predictRating(userMap, updatedDeviationMatrix, (7, 103)))
  println("item: 106 " + new Algorithms().predictRating(userMap, updatedDeviationMatrix, (7, 106)))
  println("==============================END prediction user7==================================================")*/

  println("==========================Top Recommendation user 186 =========================================")
  var recommendations = h.time{h.recommendationsRecursive(6,userMap,deviationMatrix,5)}
  recommendations.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("==========================End Top Recommendation===============================================")





  //val updatedUserMap = h.addNewItemToUser(6,107,4.5,userMap)

/*  println("----------print only the users in the usermap---------------------")
  println("usermap size " + userMap.size)
  println(userMap.foreach(x => println("user: " + x._1)))

  println("----------print only the users in the updated usermap---------------------")
  println("usermap size " + userMap.size)
  println(userMap.foreach(x => println("user: " + x._1)))

  println("----------print only the items for user 3--------------------------------")
  println(userMap.get(3).get.ratings.foreach(x => println(s"item: ${x._1} rating: ${x._2}")))

  println("----------print only the items for user 3 after update--------------------------------")
  println(updatedUserMap.get(3).get.ratings.foreach(x => println(s"item: ${x._1} rating: ${x._2}")))*/

/*
  println("--------------Test before update item----------------------------")
  println(userMap.foreach(x => println("user: " +x._1 + "\n" + x._2.ratings.foreach(y => println(s"item: ${y._1} has a rating: ${y._2}")))))
  println("--------------Test new update item----------------------------")
  println(updatedUserMap.foreach(x => println("user: " +x._1 + "\n" + x._2.ratings.foreach(y => println(s"item: ${y._1} has a rating: ${y._2}")))))
*/

/*  println("deviationMatrix")
  h.printDeviationMatrix(h.calculateAllDeviations(userMap))

  println("updatedDeviationMatrix")
  h.printDeviationMatrix(h.updateDeviationMatrix(userMap,h.calculateAllDeviations(userMap),(3,105,4.0)))*/
//  println("1--------------------------------")
  //println(new Algorithms().predictRating(userMap, h.calculateAllDeviations(userMap), (7, 103)))
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
/*    println("-------------------------------------------------------")
    println("Recommendation session")
    val recommendations = h.recommendationsRecursive(7,userMap,h.calculateAllDeviations(userMap), 1)
    recommendations.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
    println("-------------------------------------------------------")*/
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
