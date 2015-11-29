package gdx.scala.colordash.physics

import com.badlogic.gdx.math.Rectangle
import gdx.scala.colordash.effect.Effects
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles._
import gdx.scala.colordash.Constants

import gdx.scala.colordash.tiles.Spike

import scala.collection.JavaConversions._

trait GravityPhysics {
  protected implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]

  val gravity: Float

  def collide(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    collideX(player)
    collideY(player)
  }

  def updateVelocity(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    updateVelocityX(player)
    updateVelocityY(player)
  }

  protected def collideY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  protected def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit

  private def collideX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val rect = player.rect
    val velocity = player.velocity

    Tile.findTiles(
      rect.x.toInt,
      rect.y.toInt,
      (futureRect.x + Constants.tileWidth).toInt,
      (rect.y + Constants.tileHeigth).toInt)

    val collidingTile = tiles.filterNot { tile => tile.has[Nothing] || tile.has[Spike] }.find(_.overlaps(futureRect))
    collidingTile match {
      case Some(tile) => futureRect.x = tile.x - futureRect.width
      case _ =>
    }

    Tile.freeAll(tiles)
  }

  private def updateVelocityX(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val velocity = player.velocity
    val futureTile = Tile(futureRect)
    val nextTile = futureTile.tileRight

    nextTile.content match {
      case _: Nothing => if (0 != velocity.x && velocity.x < player.baseVelocity) {
        velocity.x = player.baseVelocity
      } else if (player.baseVelocity < velocity.x) {
        velocity.x += Constants.friction * delta
      }
      case _ =>
        velocity.x = 0
    }
    Tile.free(nextTile)
    Tile.free(futureTile)
  }

  def processActions(player: Player): Unit = {
    tiles.clear()

    val currentTile = Tile(player.rect)
    tiles.add(currentTile.tileRight)
    tiles.add(currentTile.tileUp)
    tiles.add(currentTile.tileDown)

    tiles.filter { tile =>
      tile.has[Activator] &&
        tile.touches(player.rect)
    }.foreach { tile =>
      tile.effect.applyEffect(player)
      tile.effect = Effects.None
    }

    Tile.freeAll(tiles)
  }
}

object NormalGravityPhysics extends GravityPhysics {

  val gravity = Constants.gravity * -1

  protected def collideY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
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

    Tile.findTiles(
      rect.x.toInt,
      startY.toInt,
      (rect.x + Constants.tileWidth).toInt,
      endY.toInt)

    val collidingTile = tiles.filterNot { tile => tile.has[Nothing] || tile.has[Spike] }.find(_.overlaps(futureRect))
    collidingTile match {
      case Some(tile) => if (0 < velocity.y) {
        futureRect.y = tile.y - Constants.tileHeigth
      } else {
        futureRect.y = tile.y + Constants.tileHeigth
      }
      case _ =>
    }

    Tile.freeAll(tiles)
  }

  protected def updateVelocityY(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {

    val velocity = player.velocity
    val futureTile = Tile(futureRect)
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    (tileUp.has[Nothing], tileDown.has[Nothing]) match {
      case (false, _) if 0 < velocity.y => velocity.y = 0
      case (_, false) if velocity.y < 0 && futureRect.y <= tileDown.y + Constants.tileHeigth => velocity.y = 0
      case (_, _) => velocity.y += gravity * delta
    }

    Tile.free(tileUp)
    Tile.free(tileDown)
    Tile.free(futureTile)
  }

}

//object ReversedGraityPhysics extends GravityPhysics {

//}