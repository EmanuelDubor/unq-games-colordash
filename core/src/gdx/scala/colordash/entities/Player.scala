package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._

import scala.collection.JavaConversions._

class Player extends SquaredEntity {
  val INITIAL_VELOCITY = 5
  var velocity = new Vector2(INITIAL_VELOCITY, 0)
  var effectState: EffectState = EffectNone
  var movementState: EffectState = EffectFalling
  val playerTexture = TextureRegion.split(new Texture("boxes_map.png"), 64, 64)(0)(0)
  private var tiles: com.badlogic.gdx.utils.Array[Rectangle] = new com.badlogic.gdx.utils.Array[Rectangle]

  override val color = new Color(0xb25656ff)

  rect.width = 1f
  rect.height = 1f
  rect.y = 7f

  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, rect.width, rect.height)
  }

  def update(delta: Float): Unit = {

    movementState.applyEffect(this)
    processActions()
    effectState.applyEffect(this)

    val futureRect = TiledWorld.rectPool.obtain()
    //    velocity.scl(delta)
    futureRect.set(rect.x + velocity.x * delta, rect.y + velocity.y * delta, rect.width, rect.height)

    TiledWorld.findTiles(
      futureRect.x.toInt,
      rect.y.toInt,
      futureRect.x.toInt + futureRect.width.toInt,
      rect.y.toInt + rect.height.toInt
      , tiles)

    if (velocity.x > 0) {

      for (tile <- tiles) {
        if (tile.overlaps(futureRect)) {
          velocity.x = 0
        }
      }
    } else if (velocity.x == 0) {
      var isColliding = false
      for (tile <- tiles) {
        if (tile.overlaps(futureRect)) {
          isColliding = true
        }
      }
      if (!isColliding) {
        velocity.x = INITIAL_VELOCITY
      }
    }

    TiledWorld.findTiles(
      rect.x.toInt,
      futureRect.y.toInt,
      rect.x.toInt + rect.width.toInt,
      futureRect.y.toInt + futureRect.height.toInt
      , tiles)

    if (velocity.y < 0) {
      for (tile <- tiles) {
        if (tile.overlaps(futureRect)) {
          velocity.y = 0
          futureRect.y = tile.y + futureRect.height
          movementState = EffectNone
        }
      }
    } else if (velocity.y == 0) {
      var noCollisions = true
      for (tile: Rectangle <- tiles) {
        if (tile.overlaps(futureRect)) {
          noCollisions = false
        }

      }
      if (noCollisions) {
        movementState = EffectFalling
      }
    }

    rect.set(futureRect.x, futureRect.y, futureRect.width, futureRect.height)

    TiledWorld.rectPool.free(futureRect)
  }

  def processActions(): Unit = {
    TiledWorld.findTiles(
      rect.x.toInt,
      rect.y.toInt - rect.height.toInt,
      rect.x.toInt + rect.width.toInt,
      rect.y.toInt + rect.height.toInt
      , tiles, "activator")

    if (tiles.size != 0) {
      effectState = EffectJump
    } else {
      effectState = EffectFalling
    }
  }

  def tilesBelow(position: Rectangle): Unit = {

  }

}
