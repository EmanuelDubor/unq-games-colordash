package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._
import gdx.scala.colordash.Effects._

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

    implicit val futureRect = TiledWorld.rectPool.obtain()
    futureRect.set(rect.x + velocity.x * delta, rect.y + velocity.y * delta, rect.width, rect.height)

    collisionXAxis
    collisionYAxis

    rect.set(futureRect.x, futureRect.y, futureRect.width, futureRect.height)

    TiledWorld.rectPool.free(futureRect)
  }

  private def collisionYAxis(implicit futureRect: Rectangle): Unit = {
    TiledWorld.findTiles(
      rect.x.toInt,
      futureRect.y.toInt,
      rect.x.toInt + rect.width.toInt,
      futureRect.y.toInt + futureRect.height.toInt
      , tiles)

    val isColliding = tiles.exists(_.overlaps(futureRect))
    if (isColliding && velocity.y < 0) {
      velocity.y = 0
      val collidingTile = tiles.find(_.overlaps(futureRect)).get
      futureRect.y = collidingTile.y + futureRect.height
      movementState = EffectNone
    } else if (!isColliding && velocity.y == 0) {
      movementState = EffectFalling
    }
  }

  private def collisionXAxis(implicit futureRect: Rectangle): Unit = {
    TiledWorld.findTiles(
      futureRect.x.toInt,
      rect.y.toInt,
      futureRect.x.toInt + futureRect.width.toInt,
      rect.y.toInt + rect.height.toInt
      , tiles)

    val isColliding = tiles.exists(_.overlaps(futureRect))
    if (isColliding && velocity.x > 0) {
      velocity.x = 0
    } else if (!isColliding && velocity.x == 0) {
      velocity.x = INITIAL_VELOCITY
    }
  }

  def processActions(): Unit = {
    TiledWorld.findTiles(
      rect.x.toInt,
      rect.y.toInt - rect.height.toInt,
      rect.x.toInt + rect.width.toInt,
      rect.y.toInt + rect.height.toInt
      , tiles, "activator")

    if (tiles.nonEmpty) {
      effectState = EffectJump
    } else {
      effectState = EffectFalling
    }
  }

}
