package gdx.scala.colordash

import com.badlogic.gdx.math.Rectangle
import gdx.scala.colordash.entities.Player

import scala.collection.JavaConversions._

trait GravityPhysics {
  val tiles = new com.badlogic.gdx.utils.Array[Rectangle]

  val gravity: Float

  def collideY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  def collideX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val rect = player.rect
    val velocity = player.velocity

    TiledWorld.findTiles(
      rect.x.toInt,
      rect.y.toInt,
      (futureRect.x + Constants.tileWidth).toInt,
      (rect.y + Constants.tileHeigth).toInt,
      tiles)

    val isColliding = tiles.exists(_.overlaps(futureRect))
    if (isColliding) {
      val collidingTile = tiles.find(_.overlaps(futureRect)).get
      futureRect.x = collidingTile.x - futureRect.width
    }

    TiledWorld.rectPool.freeAll(tiles)
  }

  def updateVelocityX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val velocity = player.velocity

    val nextTile = TiledWorld.getTile(
      (futureRect.x + Constants.tileWidth).toInt,
      futureRect.y.toInt
    )

    nextTile match {
      case None => if (0 != velocity.x && velocity.x < player.baseVelocity) {
        velocity.x = player.baseVelocity
      } else if (player.baseVelocity < velocity.x) {
        velocity.x += Constants.friction * delta
      }
      case Some(rect) =>
        velocity.x = 0
        TiledWorld.rectPool.free(rect)
    }
  }
}

object NormalGravityPhysics extends GravityPhysics {

  val gravity = Constants.gravity * -1

  def collideY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val rect = player.rect
    val velocity = player.velocity
    var yAux = futureRect.y

    if (0 < velocity.y) {
      yAux += Constants.tileHeigth
    } else {
      yAux -= Constants.tileHeigth
    }

    val startY = Math.min(rect.y, yAux)
    val endY = Math.max(rect.y, yAux)

    TiledWorld.findTiles(
      rect.x.toInt,
      startY.toInt,
      (rect.x + Constants.tileWidth).toInt,
      endY.toInt,
      tiles)

    val isColliding = tiles.exists(_.overlaps(futureRect))
    if (isColliding) {
      val collidingTile = tiles.find(_.overlaps(futureRect)).get
      if (0 < velocity.y) {
        futureRect.y = collidingTile.y - Constants.tileHeigth
      } else {
        futureRect.y = collidingTile.y + Constants.tileHeigth
      }
    }
    TiledWorld.rectPool.freeAll(tiles)
  }

  def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {

    val velocity = player.velocity

    val topTile = TiledWorld.getTile(
      futureRect.x.toInt,
      (futureRect.y + Constants.tileHeigth).toInt
    )

    val bottomTile = TiledWorld.getTile(
      futureRect.x.toInt,
      (futureRect.y - Constants.tileHeigth).toInt
    )

    (topTile, bottomTile) match {
      case (Some(_), _) if 0 < velocity.y => velocity.y = 0
      case (_, Some(_)) if velocity.y < 0 => velocity.y = 0
      case (_, _) => velocity.y += gravity * delta
    }
    topTile.foreach(TiledWorld.rectPool.free(_))
    bottomTile.foreach(TiledWorld.rectPool.free(_))
  }

}

//object ReversedGraityPhysics extends GravityPhysics {

//}