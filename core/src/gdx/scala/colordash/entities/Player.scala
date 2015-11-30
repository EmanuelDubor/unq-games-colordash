package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._
import gdx.scala.colordash.physics.{GravityPhysics, NormalGravityPhysics}
import gdx.scala.colordash.tiles.{Spike, Tile}

class Player(playerTexture: TextureRegion) extends SquaredEntity {
  private var physicsComponent: GravityPhysics = NormalGravityPhysics
  private implicit val futureRect = new Rectangle().setSize(Constants.tileWidth, Constants.tileHeigth)

  var baseVelocity = Constants.initialVelocity
  private var tickCount = 0

  var totalTime=0f

  val velocity = new Vector2(baseVelocity, 0)
  rect.width = Constants.tileWidth
  rect.height = Constants.tileHeigth
  rect.x = Constants.startX
  rect.y = Constants.startY


  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, Constants.tileWidth, Constants.tileHeigth)
  }

  def update(implicit delta: Float): Unit = {
    totalTime+=delta
    physicsComponent.processActions(this)

    futureRect.setPosition(rect.x + velocity.x * delta, rect.y + velocity.y * delta)

    physicsComponent.collide(this)
    physicsComponent.updateVelocity(this)
    checkDefeat

    rect.setPosition(futureRect.x, futureRect.y)
  }

  def checkDefeat: Unit = {
    val futureTile = Tile(futureRect.x.toInt, futureRect.y.toInt)

    if (rect.equals(futureRect) || futureTile.has[Spike]) {
      tickCount += 1
    } else {
      tickCount = 0
    }

    if (Constants.stuckLimit < tickCount) {
      ColorDashGame.newPlayer
    }
    Tile.free(futureTile)
  }

}



