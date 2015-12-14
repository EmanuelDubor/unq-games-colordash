package gdx.scala.colordash.effect

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.Constants._
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiles.Tile
import gdx.scala.colordash.utils.LifeCycle

trait Effect extends LifeCycle {
  val color: Color

  var pixmap: Pixmap = _
  var texture: Texture = _

  def applyEffect(player: Player, triggeringTile: Tile): Unit

  def render(batch: Batch, x: Int, y: Int) = {
    batch.draw(texture, x, y, Constants.tileWidth, Constants.tileHeight)
  }

  def create() = {
    pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()
    texture = new Texture(pixmap)
  }

  def dispose() = {
    pixmap.dispose()
    texture.dispose()
  }
}

object Effects extends LifeCycle {

  val all = List(Blink, SmallJump, Dash, ReverseGravity, None)

  object Blink extends Effect {
    val color: Color = Color.RED

    def applyEffect(player: Player, triggeringTile: Tile): Unit = {
      val playerTile = player.currentTile()
      if (playerTile.isLeft(triggeringTile)) {
        val targetTile = triggeringTile.tileRight
        applyBlink(player, targetTile)
        Tile.free(targetTile)
      } else if (playerTile.isAbove(triggeringTile)) {
        val targetTile = triggeringTile.tileDown
        applyBlink(player, targetTile)
        Tile.free(targetTile)
      } else if (playerTile.isUnder(triggeringTile)) {
        val targetTile = triggeringTile.tileUp
        applyBlink(player, targetTile)
        Tile.free(targetTile)
      }
      Tile.free(playerTile)
    }

    protected def applyBlink(player: Player, targetTile: Tile): Unit = {
      if (!targetTile.isSolid) {
        player.setPosition(targetTile)
      }
    }
  }

  object SmallJump extends Effect {
    val color: Color = Color.MAGENTA

    def applyEffect(player: Player, triggeringTile: Tile): Unit = {
      player.addVelocity(smallJumpX, smallJumpY)
    }
  }

  object Dash extends Effect {
    val color: Color = Color.CYAN

    def applyEffect(player: Player, triggeringTile: Tile): Unit = {
      player.addVelocity(dashX, dashY)
    }
  }

  object ReverseGravity extends Effect {
    val color: Color = Color.YELLOW

    def applyEffect(player: Player, triggeringTile: Tile): Unit = {
      player.physicsComponent = player.physicsComponent.reversedGravity()
    }
  }

  object None extends Effect {
    val color: Color = Color.WHITE

    def applyEffect(player: Player, triggeringTile: Tile): Unit = {}
  }

  override def create(): Unit = all.foreach(_.create())

  override def dispose(): Unit = all.foreach(_.dispose())
}