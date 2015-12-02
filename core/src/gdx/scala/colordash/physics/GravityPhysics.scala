package gdx.scala.colordash.physics

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.effect.Effects
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles.TileContent._
import gdx.scala.colordash.tiles._

import scala.collection.JavaConversions._

trait GravityPhysics {
  protected implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]

  val gravity: Float

  def addTo(vector: Vector2, x: Float, y: Float): Unit

  protected def onCollideUp(player: Player)(implicit delta: Float): Unit

  protected def onCollideDown(player: Player)(implicit delta: Float): Unit

  def update(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val futureTile = Tile(futureRect)
    val tileRight = futureTile.tileRight
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    if (tileRight.isSolid && tileRight.overlaps(futureRect)) {
      futureRect.x = futureTile.x
      player.velocity.x = 0
    } else if (player.baseVelocity < player.velocity.x) {
      player.velocity.x += Constants.friction * delta
    } else {
      player.velocity.x = player.baseVelocity
    }

    if (tileUp.isSolid && tileUp.overlaps(futureRect)) {
      futureRect.y = futureTile.y
      onCollideUp(player)
    } else if (tileDown.isSolid && tileDown.overlaps(futureRect)) {
      futureRect.y = futureTile.y
      onCollideDown(player)
    } else {
      player.velocity.y += gravity * delta
    }

    Tile.free(futureTile)
    Tile.free(tileRight)
    Tile.free(tileUp)
    Tile.free(tileDown)

  }

  def processActions(player: Player): Unit = {
    tiles.clear()

    val currentTile = Tile(player.rect)
    tiles.add(currentTile.tileRight)
    tiles.add(currentTile.tileUp)
    tiles.add(currentTile.tileDown)

    tiles.filter { tile =>
      tile.has(Activator) &&
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

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y)

  protected def onCollideUp(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = gravity * delta
  }

  protected def onCollideDown(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = 0
  }
}

object ReversedGravityPhysics extends GravityPhysics {
  val gravity: Float = Constants.gravity

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y * -1)

  protected def onCollideUp(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = 0
  }

  protected def onCollideDown(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = gravity * delta
  }
}