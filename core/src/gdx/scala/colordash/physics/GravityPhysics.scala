package gdx.scala.colordash.physics

import com.badlogic.gdx.math.Rectangle
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles._
import gdx.scala.colordash.{Constants, TiledWorld}

import gdx.scala.colordash.tiles.Spike

import scala.collection.JavaConversions._

trait GravityPhysics {
  protected implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]

  val gravity: Float

  def update(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    collideX(player)
    collideY(player)
    checkSpikes(player)
    updateVelocityX(player)
    updateVelocityY(player)
  }

  def checkSpikes(player: Player)(implicit futureRect: Rectangle): Unit = {
    val currentTile = TiledWorld.getTile(futureRect.x.toInt, futureRect.y.toInt)
    currentTile match {
      case Some(tile) if tile.has[Spike] => player.defeat
      case _ =>
    }
    Tile.free(currentTile)
  }

  def collideY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  def collideX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val rect = player.rect
    val velocity = player.velocity

    TiledWorld.findTiles(
      rect.x.toInt,
      rect.y.toInt,
      (futureRect.x + Constants.tileWidth).toInt,
      (rect.y + Constants.tileHeigth).toInt)

    val collidingTile = tiles.find(_.overlaps(futureRect))
    collidingTile match {
      case Some(tile) if !tile.has[Spike] => futureRect.x = tile.x - futureRect.width
      case _ =>
    }

    Tile.freeAll(tiles)
  }

  def updateVelocityX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val velocity = player.velocity
    val futureTile = Tile(futureRect)

    val nextTile = futureTile.tileRight

    nextTile match {
      case None => if (0 != velocity.x && velocity.x < player.baseVelocity) {
        velocity.x = player.baseVelocity
      } else if (player.baseVelocity < velocity.x) {
        velocity.x += Constants.friction * delta
      }
      case Some(tile) =>
        velocity.x = 0
        Tile.free(tile)
    }
  }

  def processActions(player: Player): Unit = {
    val rect = player.rect
    TiledWorld.findTiles(
      rect.x.toInt,
      (rect.y - Constants.tileHeigth).toInt,
      (rect.x + Constants.tileWidth).toInt,
      (rect.y + Constants.tileHeigth).toInt)

    tiles.filter(_.has[Activator]).foreach(_.content.applyTo(player))
    Tile.freeAll(tiles)
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
      endY.toInt)

    val collidingTile = tiles.find(_.overlaps(futureRect))
    collidingTile match {
      case Some(tile) if !tile.has[Spike] => if (0 < velocity.y) {
        futureRect.y = tile.y - Constants.tileHeigth
      } else {
        futureRect.y = tile.y + Constants.tileHeigth
      }
      case _ =>
    }

    Tile.freeAll(tiles)
  }

  def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {

    val velocity = player.velocity
    val futureTile = Tile(futureRect)
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    (tileUp, tileDown) match {
      case (Some(_), _) if 0 < velocity.y => velocity.y = 0
      case (_, Some(tile)) if velocity.y < 0 && futureRect.y <= tile.y + Constants.tileHeigth  => velocity.y = 0
      case (_, _) => velocity.y += gravity * delta
    }
    Tile.free(tileUp)
    Tile.free(tileDown)
    Tile.free(futureTile)
  }

}

//object ReversedGraityPhysics extends GravityPhysics {

//}