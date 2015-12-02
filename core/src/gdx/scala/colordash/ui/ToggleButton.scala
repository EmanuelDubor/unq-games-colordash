package gdx.scala.colordash.ui

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}

abstract class ToggleButton(x: Int, y: Int, val textureOff: TextureRegion, val textureOn: TextureRegion) {

  var on = false

  def click(): Unit = {
    if (!on) {
      toggleOn()
    } else {
      toggleOff()
    }
  }

  def toggleOff() = {
    on = false
    onToggleOff()
  }

  def toggleOn() = {
    on = true
    onToggleOn()
  }

  def render(batch: Batch) = {
    if (on) {
      batch.draw(textureOn, x, y)
    } else {
      batch.draw(textureOff, x, y)
    }
  }

  def canHandleClick(clickX: Int, clickY: Int): Boolean =
    x <= clickX && clickX <= x + textureOn.getRegionWidth &&
      y <= clickY && clickY <= y + textureOn.getRegionHeight

  def onToggleOn(): Unit

  def onToggleOff(): Unit = {}

}
