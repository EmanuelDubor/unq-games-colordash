package gdx.scala.colordash.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import gdx.scala.colordash.effect.Effects
import gdx.scala.colordash.utils.LifeCycle
import gdx.scala.colordash.{ColorDashGame, Constants}

object GUI extends GameInputHandler with LifeCycle {
  val screeWidth = Gdx.graphics.getWidth
  val screenHeight = Gdx.graphics.getHeight
  var font: BitmapFont = _
  var guiBase: Texture = _
  var buttons: Texture = _
  var buttonsRegions: Array[Array[TextureRegion]] = _
  var guiButtons: List[ToggleButton] = _
  var pauseButton: ToggleButton = _
  var retryButton: ToggleButton = _
  var largeJumpButton: ToggleButton = _
  var smallJumpButton: ToggleButton = _
  var dashButton: ToggleButton = _
  var reverseGravityButton: ToggleButton = _

  def create() = {
    font = new BitmapFont
    font.setColor(Color.ORANGE)
    guiBase = new Texture(Constants.guiBase)
    buttons = new Texture(Constants.buttons)
    val buttonSize = 64
    buttonsRegions = TextureRegion.split(buttons, buttonSize, buttonSize)

    pauseButton = new ToggleButton(screeWidth - (1 * buttonSize), 0, buttonsRegions(0)(0), buttonsRegions(1)(0)) {
      def onToggleOn() = ColorDashGame.togglePause()

      override def onToggleOff(): Unit = ColorDashGame.togglePause()
    }
    retryButton = new ToggleButton(screeWidth - (2 * buttonSize), 0, buttonsRegions(0)(1), buttonsRegions(1)(1)) {
      def onToggleOn() = {
        ColorDashGame.newPlayer()
        toggleOff()
      }
    }
    largeJumpButton = new ToggleButton(0 * buttonSize, 0, buttonsRegions(0)(3), buttonsRegions(1)(3)) {
      def onToggleOn() = {
        currentEffect = Effects.LargeJump
        smallJumpButton.toggleOff()
        dashButton.toggleOff()
        reverseGravityButton.toggleOff()
      }
    }
    smallJumpButton = new ToggleButton(1 * buttonSize, 0, buttonsRegions(0)(4), buttonsRegions(1)(4)) {
      def onToggleOn() = {
        currentEffect = Effects.SmallJump
        largeJumpButton.toggleOff()
        dashButton.toggleOff()
        reverseGravityButton.toggleOff()
      }
    }
    dashButton = new ToggleButton(2 * buttonSize, 0, buttonsRegions(0)(5), buttonsRegions(1)(5)) {
      def onToggleOn() = {
        currentEffect = Effects.Dash
        largeJumpButton.toggleOff()
        smallJumpButton.toggleOff()
        reverseGravityButton.toggleOff()
      }
    }
    reverseGravityButton = new ToggleButton(3 * buttonSize, 0, buttonsRegions(0)(6), buttonsRegions(1)(6)) {
      def onToggleOn() = {
        currentEffect = Effects.ReverseGravity
        largeJumpButton.toggleOff()
        smallJumpButton.toggleOff()
        dashButton.toggleOff()
      }
    }

    guiButtons = List(pauseButton, retryButton, largeJumpButton, smallJumpButton, dashButton, reverseGravityButton)
  }

  def render() = {
    val batch = new SpriteBatch
    batch.begin()
    batch.draw(guiBase, 0, 0)
    guiButtons.foreach(_.render(batch))
    font.draw(batch, s"Tries: ${ColorDashGame.players.size}", 265, 48)
    font.draw(batch, s"Time: ${ColorDashGame.currentPlayer.totalTime.toInt} seconds", 265, 24)
    batch.end()
  }

  def dispose() = {
    guiBase.dispose()
    buttons.dispose()
    font.dispose()
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    if (screenY < Gdx.graphics.getHeight - guiBase.getHeight) {
      super.touchDown(screenX, screenY, pointer, button)
    } else {
      button match {
        case Buttons.LEFT => click(screenX, screenY)
        case _ => false
      }
    }
  }

  def click(screenX: Int, screenY: Int): Boolean = {
    val clickedButton = guiButtons.find(_.canHandleClick(screenX, screenHeight - screenY))
    clickedButton match {
      case Some(button) => button.click()
        true
      case _ => false
    }
  }
}
