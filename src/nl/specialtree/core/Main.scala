package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  //for the print stuff
  val notFunctional = "Not Functional"
  val functional = "Functional"
  //Import user and compute the deviation matrix
  val userMap:Map[Int, UserPref] = new LoadDataService() loadSmallDataset()
  val h:ReallyHandyToolsMustUseThisClassForBestResults = new ReallyHandyToolsMustUseThisClassForBestResults()
  val f:HandyToolFunctional = new HandyToolFunctional()
  println("========================== DevMatrix Computation Section =========================================")
  println(notFunctional)
  val deviationMatrixNotFunctional:Map[Int,ItemReference] = h.time{h.newCalculateAllDeviations(userMap)}
  println(functional)
  val deviationMatrixFunctional = f.time{f.calcDeviations(userMap)}
  println("=======================END DevMatrix Computation Section =========================================")
  println(" ")

  println("========================== Prediction Section =========================================")
  println(notFunctional)
  println("item: 103 " + new Algorithms().predictRating(userMap, deviationMatrixNotFunctional, (7, 103)))
  println("item: 106 " + new Algorithms().predictRating(userMap, deviationMatrixNotFunctional, (7, 106)))
  println(functional)
  println("item: 103 " + f.predictRating(userMap.get(7).get, deviationMatrixFunctional, 103))
  println("item: 106 " + f.predictRating(userMap.get(7).get, deviationMatrixFunctional, 106))

  println("==========================User 3 Updates Item 105 with 4.0 =========================================")
  val nonFunctionalUpdateDeviationMatrix = h.time{h.updateDeviationMatrix(userMap,deviationMatrixNotFunctional,(3,105,4.0))}
  val functionalUpdateDeviationMatrix = f.time{f.updateDeviationMatrix(userMap,deviationMatrixFunctional,(3,105,4.0))}
  println("not functional updated")
  println("item: 103 " + new Algorithms().predictRating(userMap, deviationMatrixNotFunctional, (7, 103)))
  println("item: 106 " + new Algorithms().predictRating(userMap, deviationMatrixNotFunctional, (7, 106)))
  println("functional updated")
  println("item: 103 " + f.predictRating(userMap.get(7).get, deviationMatrixFunctional, 103))
  println("item: 106 " + f.predictRating(userMap.get(7).get, deviationMatrixFunctional, 106))

  println("========================== END Prediction Section =========================================")

  println(" ")
  println("===========================Recommendation section ====================================================")
  println("=============================user 7 Non Functional Update method =======================================")
  val a = h.time{h.recommendations(7,userMap,deviationMatrixNotFunctional,10)}
  a.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END user 7 Non Functional Update method====================================")
  println("=============================user 7 Functional Update method ===========================================")
  val b = f.time{f.recommendations(7,userMap,deviationMatrixFunctional,10)}
  b.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END User 7 Functional Update method========================================")
  println("========================END Recommendation section ====================================================")

/*  println("===========================Recommendation section ====================================================")
  println("=============================user 186 Non Functional Update method =======================================")
  val aa = h.time{h.recommendations(186,userMap,deviationMatrixNotFunctional,10)}
  aa.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END user 186 Non Functional Update method====================================")
  println("=============================user 186 Functional Update method ===========================================")
  val bb = f.time{f.recommendations(186,userMap,deviationMatrixFunctional,10)}
  bb.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END User 186 Functional Update method========================================")*/











  /*
    //#########TESTING THE PREDICTION METHODS#################################
    println("item: 103 " + new Algorithms().predictRating(userMap, deviationMatrix, (7, 103)))
    println("item: 103 " + new Algorithms().newPredictRating(userMap.get(7).get, deviationMatrix, 103))
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

/*  println("==========================Top Recommendation user 186 NEW METHOD=========================================")
  var recommendations2 = h.time{h.recommendations(186,userMap,deviationMatrix,10)}
  recommendations2.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("==========================End Top Recommendation RECURSIVE===============================================")*/

/*  println("=================================Non Functional Update method ===========================================")
  val updatedDeviationMatrix:Map[Int,ItemReference] = h.time{h.updateDeviationMatrix(userMap,deviationMatrix,(3,105,4.0))}
  println("=============================END Non Functional Update method ===========================================")
  println("==================================== Functional Update method ===========================================")
  val functionalUpdatedDeviationMatrix:Map[Int,ItemReference] = f.time{f.updateDeviationMatrix(userMap,deviationMatrix,(3,105,4.0))}
  println("=================================END Functional Update method ===========================================")
  println(" ")
  println("=================================Non Functional Update method ===========================================")
  val x = h.time{new Algorithms().newPredictRating(userMap.get(7).get, updatedDeviationMatrix,101)}
  println("item: 101 " + new Algorithms().newPredictRating(userMap.get(7).get, updatedDeviationMatrix,101))
  println("item: 103 " + new Algorithms().newPredictRating(userMap.get(7).get, updatedDeviationMatrix,103))
  println("=============================END Non Functional Update method ===========================================")
  println("==================================== Functional Update method ===========================================")
  val y = f.time{f.predictRating(userMap.get(7).get, updatedDeviationMatrix,101)}
  println("item: 101 " + f.predictRating(userMap.get(7).get, updatedDeviationMatrix,101))
  println("item: 103 " + f.predictRating(userMap.get(7).get, updatedDeviationMatrix,103))
  println("================================END  Functional Update method ===========================================")
  println(" ")
  println("=================================Non Functional Update method ===========================================")
  val a = h.time{h.recommendations(7,userMap,deviationMatrix,5)}
  a.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END Non Functional Update method ===========================================")
  println("==================================== Functional Update method ===========================================")
  val b = f.time{f.recommendations(7,userMap,deviationMatrix,5)}
  b.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("================================END  Functional Update method ===========================================")*/

/*
  println("=============================user 186 Non Functional Update method =======================================")
  val a = h.time{h.recommendations(7,userMap,deviationMatrix,10)}
  a.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END user 186 Non Functional Update method====================================")
  println("=============================user 186 Functional Update method ===========================================")
  val b = f.time{f.recommendations(7,userMap,deviationMatrix,10)}
  b.foreach(result => println(s"item: ${result._1} rating: ${result._2}"))
  println("=============================END User 186 Functional Update method========================================")
*/

}
