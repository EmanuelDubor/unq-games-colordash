package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash._
import gdx.scala.colordash.physics._
import gdx.scala.colordash.tiles.{Tile, TileContent}

class Player(playerTexture: TextureRegion) extends SquaredEntity {
  var physicsComponent: GravityPhysics = NormalGravityPhysics
  val futureRect = new Rectangle().setSize(Constants.tileWidth, Constants.tileHeight)

  var baseVelocity = Constants.initialVelocity
  private var stuckTick = 0
  private var spikeTick = 0

  var totalTime = 0f

  val velocity = new Vector2(baseVelocity, 0)
  rect.width = Constants.tileWidth
  rect.height = Constants.tileHeight
  rect.x = Constants.startX
  rect.y = Constants.startY

  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, Constants.tileWidth, Constants.tileHeight)
  }

  def update(implicit delta: Float): Unit = {
    totalTime += delta
    physicsComponent.processActions(this)

    futureRect.setPosition(rect.x + velocity.x * delta, rect.y + velocity.y * delta)

    physicsComponent.update(this)
    checkDefeat()

    rect.setPosition(futureRect.x, futureRect.y)
  }

  def checkDefeat(): Unit = {
    val futureTile = Tile(futureRect)

    if (rect.equals(futureRect)) {
      stuckTick += 1
    } else {
      stuckTick = 0
    }

    if (futureTile.has(TileContent.Spike)) {
      spikeTick += 1
    } else {
      spikeTick = 0
    }

    if (Constants.maxStuck < stuckTick ||
      Constants.maxSpike < spikeTick ||
      rect.y < 0 || Constants.sectionHeight <= rect.y
    ) {
      ColorDashGame.newPlayer()
    }
    Tile.free(futureTile)
  }

  def addVelocity(x: Float, y: Float) = physicsComponent.addTo(velocity, x, y)

}



