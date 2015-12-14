package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import gdx.scala.colordash.Constants.PlayerValues._
import gdx.scala.colordash.Constants.TileValues._
import gdx.scala.colordash._
import gdx.scala.colordash.effect.Effects
import gdx.scala.colordash.physics._
import gdx.scala.colordash.tiles.TileContent.Activator
import gdx.scala.colordash.tiles.{Tile, TileContent}

import scala.collection.JavaConversions._

class Player(playerTexture: TextureRegion) extends SquaredEntity {
  var physicsComponent: GravityPhysics = NormalGravityPhysics

  val futureRect = new Rectangle().setSize(tileWidth, tileHeight)
  protected implicit val tiles = new com.badlogic.gdx.utils.Array[Tile]
  var baseVelocity = initialVelocity

  private var stuckTick = 0
  private var spikeTick = 0
  var totalTime = 0f

  val velocity = new Vector2(baseVelocity, 0)

  rect.width = tileWidth
  rect.height = tileHeight
  rect.x = startX
  rect.y = startY

  override def render(batch: Batch): Unit = {
    batch.draw(playerTexture, rect.x, rect.y, tileWidth, tileHeight)
  }

  def update(implicit delta: Float): Unit = {
    totalTime += delta
    triggerActivators()

    futureRect.setPosition(rect.x + velocity.x * delta, rect.y + velocity.y * delta)

    physicsComponent.update(this)
    checkDefeat()

    rect.setPosition(futureRect.x, futureRect.y)
  }

  def triggerActivators(): Unit = {
    tiles.clear()

    val currentTile = this.currentTile()
    tiles.add(currentTile.tileRight)
    tiles.add(currentTile.tileUp)
    tiles.add(currentTile.tileDown)

    tiles.filter { tile =>
      tile.has(Activator) &&
        tile.touches(rect)
    }.foreach { tile =>
      tile.effect.applyEffect(this, tile)
      tile.effect = Effects.None
    }

    Tile.free(currentTile)
    Tile.freeAll(tiles)
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

    if (maxStuck < stuckTick ||
      maxSpike < spikeTick ||
      rect.y < 0 || Constants.SectionManagerValues.sectionHeight <= rect.y
    ) {
      ColorDashGame.newPlayer()
    }
    Tile.free(futureTile)
  }

  def addVelocity(x: Float, y: Float) = physicsComponent.addTo(velocity, x, y)

  def setPosition(targetTile: Tile) = {
    rect.x = targetTile.x
    rect.y = targetTile.y
  }

  def currentTile(): Tile = Tile(rect)

  def futureTile(): Tile = Tile(futureRect)

}



