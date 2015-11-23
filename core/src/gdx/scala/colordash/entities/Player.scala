package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._
import gdx.scala.colordash.Effects._
import gdx.scala.colordash.tiles.Tile

import scala.collection.JavaConversions._

class Player extends SquaredEntity {
  private var physicsComponent: GravityPhysics = NormalGravityPhysics
  private implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]
  private implicit val futureRect=new Rectangle()

  override val color = new Color(0xb25656ff)
  val playerTexture = TextureRegion.split(new Texture("boxes_map.png"), 64, 64)(0)(0)

  val baseVelocity = Constants.initialVelocity
  val velocity = new Vector2(baseVelocity, physicsComponent.gravity)
  var effectState: EffectState = None

  rect.width = Constants.tileWidth
  rect.height = Constants.tileHeigth
  rect.x = 1f
  rect.y = 3.5f

  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, Constants.tileWidth, Constants.tileHeigth)
  }

  def update(implicit delta: Float): Unit = {
    processActions
    effectState.applyEffect(this)

    futureRect.set(rect.x + velocity.x * delta, rect.y + velocity.y * delta, Constants.tileWidth, Constants.tileHeigth)

    physicsComponent.collideX(this)
    physicsComponent.collideY(this)
    physicsComponent.updateVelocityX(this)
    physicsComponent.updateVelocityY(this)

    rect.set(futureRect.x, futureRect.y, Constants.tileWidth, Constants.tileHeigth)
  }

  def processActions: Unit = {
    TiledWorld.findTiles(
      rect.x.toInt,
      (rect.y - Constants.tileHeigth).toInt,
      (rect.x + Constants.tileWidth).toInt,
      (rect.y + Constants.tileHeigth).toInt)

    if (tiles.nonEmpty) {
      //      effectState = Jump
      //      effectState = Dash
    } else {
      effectState = None
    }
  }

}
