package nl.specialtree.core
/**
  * Created by razmaklat on 22-3-16.
  */
object Main extends App{
  val dataService = new LoadDataService()
  val userMap:Map[String, UserPref] = dataService loadSmallDataset()
  val a = new Hoer()
  a.updateDevationMatrix(a.calculateAllDeviations(userMap), (101, 5), (101, 4))
}
