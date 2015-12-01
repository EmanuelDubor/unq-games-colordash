package gdx.scala.colordash.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.graphics.{Color, Texture}
import gdx.scala.colordash.utils.LifeCycle
import gdx.scala.colordash.{ColorDashGame, Constants}

object GUI extends GameInputHandler with LifeCycle {
  var font: BitmapFont = _
  var texture: Texture = _

  def create() = {
    font = new BitmapFont
    font.setColor(Color.ORANGE)
    texture = new Texture(Constants.guiBase)
  }

  def render() = {
    val batch = new SpriteBatch
    batch.begin()
    batch.draw(texture, 0, 0)
    font.draw(batch, s"Tries: ${ColorDashGame.players.size}", 265, 48)
    font.draw(batch, s"Time: ${ColorDashGame.currentPlayer.totalTime.toInt} seconds", 265, 24)
    batch.end()
  }

  def dispose() = {
    texture.dispose()
    font.dispose()
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    if (screenY < Gdx.graphics.getHeight - texture.getHeight) {
      super.touchDown(screenX, screenY, pointer, button)
    } else {
      ColorDashGame.togglePause()
      true
    }
  }
}
