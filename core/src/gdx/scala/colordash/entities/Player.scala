package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._
import gdx.scala.colordash.physics.{GravityPhysics, NormalGravityPhysics}

class Player extends SquaredEntity {
  private var physicsComponent: GravityPhysics = NormalGravityPhysics
  private implicit val futureRect = new Rectangle().setSize(Constants.tileWidth, Constants.tileHeigth)

  override val color = new Color(0xb25656ff)
  val playerTexture = TextureRegion.split(new Texture("boxes_map.png"), 64, 64)(0)(0)

  var baseVelocity = Constants.initialVelocity
  val velocity = new Vector2(baseVelocity, physicsComponent.gravity)

  rect.width = Constants.tileWidth
  rect.height = Constants.tileHeigth
  rect.x = Constants.startX
  rect.y = Constants.startY

  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, Constants.tileWidth, Constants.tileHeigth)
  }

  def update(implicit delta: Float): Unit = {
    physicsComponent.processActions(this)

    futureRect.setPosition(rect.x + velocity.x * delta, rect.y + velocity.y * delta)

    physicsComponent.update(this)

    rect.setPosition(futureRect.x, futureRect.y)
  }

  def defeat = ColorDashGame.newPlayer

}



