package gdx.scala.colordash.physics

import com.badlogic.gdx.math.Vector2
import gdx.scala.colordash.Constants
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles._

import scala.collection.JavaConversions._

trait GravityPhysics {
  protected implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]

  val gravity: Float

  def addTo(vector: Vector2, x: Float, y: Float): Unit

  def reversedGravity(): GravityPhysics

  protected def onUpdateVelocityUp(player: Player)(implicit delta: Float): Unit

  protected def onUpdateVelocityDown(player: Player)(implicit delta: Float): Unit

  protected def onCollideRight(player: Player, tile: Tile): Unit = {
    player.futureRect.x = tile.x - Constants.tileWidth
  }

  protected def onCollideUp(player: Player, tile: Tile): Unit = {
    player.futureRect.y = tile.y - Constants.tileHeight
  }

  protected def onCollideDown(player: Player, tile: Tile): Unit = {
    player.futureRect.y = tile.y + Constants.tileHeight
  }

  protected def onUpdateVelocityRight(player: Player)(implicit delta: Float): Unit = {
    player.velocity.x = 0
  }

  def update(player: Player)(implicit delta: Float): Unit = {
    collideY(player)
    collideX(player)
    updateVelocity(player)
  }

  protected def collideX(player: Player): Unit = {
    val currentTile = player.currentTile()
    val futureTile = player.futureTile()

    Tile.findTiles(
      currentTile.x,
      currentTile.y,
      futureTile.x + Constants.tileWidth.toInt,
      currentTile.y + Constants.tileHeight.toInt
    )

    val collidingTile = tiles.find(tile => tile.isSolid && tile.overlaps(player.futureRect))
    collidingTile match {
      case Some(tile) => onCollideRight(player, tile)
      case None =>
    }

    Tile.free(currentTile)
    Tile.free(futureTile)
    Tile.freeAll(tiles)
  }

  protected def collideY(player: Player): Unit = {
    val currentTile = player.currentTile()
    val futureTile = player.futureTile()
    val startY = Math.min(currentTile.y, futureTile.y) - Constants.tileHeight.toInt
    var endY = Math.max(currentTile.y, futureTile.y) + Constants.tileHeight.toInt

    Tile.findTiles(
      currentTile.x,
      startY,
      currentTile.x + Constants.tileWidth.toInt,
      endY
    )

    val collidingTile = tiles.find(tile => tile.isSolid && tile.overlaps(player.futureRect))
    collidingTile match {
      case Some(tile) if 0 < player.velocity.y => onCollideUp(player, tile)
      case Some(tile) if player.velocity.y < 0 => onCollideDown(player, tile)
      case _ =>
    }

    Tile.free(currentTile)
    Tile.free(futureTile)
    Tile.freeAll(tiles)
  }

  def updateVelocity(player: Player)(implicit delta: Float): Unit = {
    val futureTile = player.futureTile()
    val tileRight = futureTile.tileRight
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    if (tileRight.isSolid && tileRight.touches(player.futureRect)) {
      onUpdateVelocityRight(player)
    } else if (player.baseVelocity < player.velocity.x) {
      player.velocity.x += Constants.friction * delta
    } else {
      player.velocity.x = player.baseVelocity
    }

    if (0 < player.velocity.y && tileUp.isSolid && tileUp.touches(player.futureRect)) {
      onUpdateVelocityUp(player)
    } else if (player.velocity.y < 0 && tileDown.isSolid && tileDown.touches(player.futureRect)) {
      onUpdateVelocityDown(player)
    } else {
      player.velocity.y += gravity * delta
    }

    Tile.free(futureTile)
    Tile.free(tileRight)
    Tile.free(tileUp)
    Tile.free(tileDown)

  }

}

object NormalGravityPhysics extends GravityPhysics {
  val gravity = Constants.gravity * -1

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y)

  protected def onUpdateVelocityUp(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = gravity * delta
  }

  protected def onUpdateVelocityDown(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = 0
  }

  def reversedGravity(): GravityPhysics = ReversedGravityPhysics
}

object ReversedGravityPhysics extends GravityPhysics {
  val gravity: Float = Constants.gravity

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y * -1)

  protected def onUpdateVelocityUp(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = 0
  }

  protected def onUpdateVelocityDown(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = gravity * delta
  }

  def reversedGravity(): GravityPhysics = NormalGravityPhysics
}