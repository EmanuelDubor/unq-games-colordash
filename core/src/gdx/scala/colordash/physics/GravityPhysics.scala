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

  def update(player: Player)(implicit futureRect: Rectangle, delta: Float): Unit = {
    val futureTile = Tile(futureRect)
    val tileRight = futureTile.tileRight
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    if (tileRight.isSolid && tileRight.overlaps(futureRect)) {
      futureRect.x = futureTile.x
    }
    if (tileUp.isSolid && tileUp.overlaps(futureRect)) {
      futureRect.y = futureTile.y
    }
    if (tileDown.isSolid && tileDown.overlaps(futureRect)) {
      futureRect.y = futureTile.y
    }

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
}

object ReversedGravityPhysics extends GravityPhysics {
  val gravity: Float = Constants.gravity

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y * -1)
}