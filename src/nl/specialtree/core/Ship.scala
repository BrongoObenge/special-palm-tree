package nl.specialtree.core

/**
  * Created by razmaklat on 22-4-16.
  */
class Ship(val coordination:(Float,Float) = (0,0),val projectiles:List[(Float,Float)] = List()) {
  //Class assignment 4.
  /*  A ship is a record containin a position, which is a pair of floating point numbers,
  and a list of projectiles positions. Write a function "update" that returns a ship
    where the first component of each projectile position has been increased by
    0.1 and the second by 0.3.*/

/*def updateShip(): Ship = {
  Ship.this
  def updateShipRec(projectiles:List[(Float,Float)],index:Int,returnedShip:Ship):Ship = {
    if(index > projectiles.length - 1) {
      returnedShip
    } else {
      val currentProjectile = projectiles(index)
      val newX = currentProjectile._1 + 0.1
      val newY = currentProjectile._2 + 0.3
      val newProjectile = projectiles.filterNot(x => x._1 == currentProjectile._1 && x._2 == currentProjectile._2)
        .::(newX,newY)
      returnedShip = new Ship(this.coordination,newProjectile)
    }
  }
}*/
}
