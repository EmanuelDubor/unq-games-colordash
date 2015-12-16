package gdx.scala.colordash.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import gdx.scala.colordash.Constants.PhysicsValues
import gdx.scala.colordash.Constants.TileValues._
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles._

import scala.collection.JavaConversions._

trait GravityPhysics {
  protected implicit val tiles = new Array[Tile]

  val gravity: Float

  def addTo(vector: Vector2, x: Float, y: Float): Unit

  def reversedGravity(): GravityPhysics

  protected def onUpdateVelocityUp(player: Player)(implicit delta: Float): Unit

  protected def onUpdateVelocityDown(player: Player)(implicit delta: Float): Unit

  protected def onCollideRight(player: Player, tile: Tile): Unit = {
    player.futureRect.x = tile.x - tileWidth
  }

  protected def onCollideUp(player: Player, tile: Tile): Unit = {
    player.futureRect.y = tile.y - tileHeight
  }

  protected def onCollideDown(player: Player, tile: Tile): Unit = {
    player.futureRect.y = tile.y + tileHeight
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
      futureTile.x + tileWidth.toInt,
      currentTile.y + tileHeight.toInt
    )

    val collidingTile = tiles.find(tile => tile.isSolid && tile.overlaps(player.futureRect))
    collidingTile match {
      case Some(tile) =>
        Gdx.app.log("ColorDash", "On collision Right")
        onCollideRight(player, tile)
      case None =>
    }

    Tile.free(currentTile)
    Tile.free(futureTile)
    Tile.freeAll(tiles)
  }

  protected def collideY(player: Player): Unit = {
    val playerTile = player.currentTile()
    val futureTile = player.futureTile()
    val startY = Math.min(playerTile.y, futureTile.y) - tileHeight.toInt
    var endY = Math.max(playerTile.y, futureTile.y) + tileHeight.toInt

    Tile.findTiles(
      playerTile.x,
      startY,
      playerTile.x + tileWidth.toInt,
      endY
    )

    val collidingTile = tiles.find(tile => tile.isSolid && tile.overlaps(player.futureRect))
    collidingTile match {
      case Some(tile) if playerTile.isUnder(tile) =>
        Gdx.app.log("ColorDash", "On collision Up")
        onCollideUp(player, tile)
      case Some(tile) if playerTile.isAbove(tile) => onCollideDown(player, tile)
      case _ =>
    }

    Tile.free(playerTile)
    Tile.free(futureTile)
    Tile.freeAll(tiles)
  }

  def updateVelocity(player: Player)(implicit delta: Float): Unit = {
    val futureTile = player.futureTile()
    val tileRight = futureTile.tileRight
    val tileUp = futureTile.tileUp
    val tileDown = futureTile.tileDown

    if (tileRight.isSolid && tileRight.touches(player.futureRect)) {
      Gdx.app.log("ColorDash", "Setting speed to zero")
      onUpdateVelocityRight(player)
    } else if (player.baseVelocity < player.velocity.x) {
      Gdx.app.log("ColorDash", "Changing speed to base")
      player.velocity.x += PhysicsValues.friction * delta
    } else {
      Gdx.app.log("ColorDash", "Setting speed to base")
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
  val gravity = PhysicsValues.gravity * -1

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
  val gravity: Float = PhysicsValues.gravity

  def addTo(vector: Vector2, x: Float, y: Float): Unit = vector.add(x, y * -1)

  protected def onUpdateVelocityUp(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = 0
  }

  protected def onUpdateVelocityDown(player: Player)(implicit delta: Float): Unit = {
    player.velocity.y = gravity * delta
  }

  def reversedGravity(): GravityPhysics = NormalGravityPhysics
}