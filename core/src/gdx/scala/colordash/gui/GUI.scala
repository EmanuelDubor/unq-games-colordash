package gdx.scala.colordash.gui

import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import gdx.scala.colordash.ColorDashGame
import gdx.scala.colordash.utils.LifeCycle

object GUI extends LifeCycle {
  var font: BitmapFont = _
  var texture: Texture = _
  var base: TextureRegion = _

  def create() = {
    font = new BitmapFont
    font.setColor(Color.ORANGE)
    texture = new Texture("gui_base.png")
    base = new TextureRegion(texture)
  }

  def render() = {
    val batch = new SpriteBatch
    batch.begin()
    batch.draw(base, 0, 0, base.getRegionWidth, base.getRegionHeight)
    font.draw(batch, s"Tries: ${ColorDashGame.players.size}", 265, 48)
    font.draw(batch, s"Time: ${ColorDashGame.currentPlayer.totalTime.toInt} seconds", 265, 24)
    batch.end()
  }

  def dispose() = {
    texture.dispose()
    font.dispose()
  }

}
