package gdx.scala.colordash

import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3
import gdx.scala.colordash.effects.{EffectState, Effects}
import gdx.scala.colordash.tiles.Activator

trait BasicInputHandler extends InputProcessor {
  def keyTyped(character: Char): Boolean = {
    false
  }

  def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    false
  }

  def keyDown(keycode: Int): Boolean = {
    false
  }

  def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    false
  }

  def keyUp(keycode: Int): Boolean = {
    false
  }

  def scrolled(amount: Int): Boolean = {
    false
  }

  def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    false
  }

  def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    false
  }
}

object InputHandler extends BasicInputHandler {
  var currentEffect: EffectState = Effects.Jump

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    button match {
      case Buttons.LEFT =>
        val gameCoords = ColorDashGame.camera.unproject(new Vector3(screenX, screenY, 0))
        val clickedTile = TiledWorld.getTile(gameCoords.x.toInt, gameCoords.y.toInt)
        clickedTile match {
          case Some(tile) if tile.has[Activator] => tile.setProperty("effect", currentEffect)
          case _ =>
        }
      case _ =>
    }
    false
  }
}
