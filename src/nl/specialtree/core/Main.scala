package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  //Import user and compute the deviation matrix
  val userMap:Map[Int, UserPref] = new LoadDataService() loadSmallDataset()
  val h:ReallyHandyToolsMustUseThisClassForBestResults = new ReallyHandyToolsMustUseThisClassForBestResults()
  //val deviationMatrix:Map[Int,ItemReference] = h.calculateAllDeviations(userMap)
  val otherDeviationMatrix:Map[Int,ItemReference] = h.newCalculateAllDeviations(userMap)

/*
  //#########TESTING THE PREDICTION METHODS#################################
  println("item: 103 " + new Algorithms().predictRating(userMap, otherDeviationMatrix, (7, 103)))
  println("item: 103 " + new Algorithms().newPredictRating(userMap.get(7).get, otherDeviationMatrix, 103))
  //######END TESTING THE PREDICTION METHODS################################
*/

  /*println(otherDeviationMatrix.size)
  println("===================Old method ===============================")
  h.printDeviationMatrix(deviationMatrix)
  println("===================New method=======================")
  h.printDeviationMatrix(otherDeviationMatrix)*/
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

  println("==========================Top Recommendation user 186 NEW METHOD=========================================")
  var recommendations2 = h.time{h.newRecommendations(186,userMap,otherDeviationMatrix,10)}
  recommendations2.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("==========================End Top Recommendation RECURSIVE===============================================")
}
