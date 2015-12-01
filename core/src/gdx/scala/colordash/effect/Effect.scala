package gdx.scala.colordash.effect

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import gdx.scala.colordash.Constants._
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.{Constants, LifeCycle}

trait Effect extends LifeCycle {
  val color: Color

  var pixmap: Pixmap = _
  var texture: Texture = _

  def applyEffect(player: Player): Unit

  def render(batch: Batch, x: Int, y: Int) = {
    batch.draw(texture, x, y, Constants.tileWidth, Constants.tileHeigth)
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

  val all = List(LargeJump, SmallJump, Dash, None)

  object LargeJump extends Effect {
    val color: Color = Color.RED

    def applyEffect(player: Player): Unit = {
      player.velocity.add(largeJumpX, largeJumpY)
    }
  }

  object SmallJump extends Effect {
    val color: Color = Color.MAGENTA

    def applyEffect(player: Player): Unit = {
      player.velocity.add(smallJumpX, smallJumpY)
    }
  }

  object Dash extends Effect {
    val color: Color = Color.CYAN

    def applyEffect(player: Player): Unit = {
      player.velocity.add(dashX, dashY)
    }
  }

  object None extends Effect {
    val color: Color = Color.WHITE

    override def applyEffect(player: Player): Unit = {}
  }

  override def create(): Unit = all.foreach(_.create())

  override def dispose(): Unit = all.foreach(_.dispose())
}