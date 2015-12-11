package gdx.scala.colordash.ui

import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.{Input, InputProcessor}
import gdx.scala.colordash.ColorDashGame
import gdx.scala.colordash.effect.{Effect, Effects}
import gdx.scala.colordash.tiles.{Tile, TileContent}

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

trait GameInputHandler extends BasicInputHandler {
  var currentEffect: Effect = Effects.None

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    button match {
      case Buttons.LEFT =>
        val gameCoords = ColorDashGame.gameCamera.unproject(new Vector3(screenX, screenY, 0))
        val clickedTile = Tile(gameCoords.x.toInt, gameCoords.y.toInt)
        if (clickedTile.has(TileContent.Activator)) {
          clickedTile.effect = currentEffect
        }
        Tile.free(clickedTile)
      case _ =>
    }
    true
  }

  override def keyUp(keycode: Int): Boolean = {
    keycode match {
      case Input.Keys.NUM_1 => GUI.largeJumpButton.click()
      case Input.Keys.NUM_2 => GUI.smallJumpButton.click()
      case Input.Keys.NUM_3 => GUI.dashButton.click()
      case Input.Keys.NUM_4 => GUI.reverseGravityButton.click()
      case Input.Keys.P => GUI.pauseButton.click()
      case Input.Keys.R => GUI.retryButton.click()
      case _ =>
    }
    true
  }

}
