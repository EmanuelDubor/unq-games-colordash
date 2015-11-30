package gdx.scala.colordash.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

object GUI {

  def render(): Unit = {
    val shapeRederer = new ShapeRenderer

    shapeRederer.begin(ShapeType.Filled)
    shapeRederer.setColor(Color.DARK_GRAY)
    shapeRederer.rect(0, 0, Gdx.graphics.getWidth, 64)
    shapeRederer.end

    shapeRederer.begin(ShapeType.Filled)
    shapeRederer.setColor(Color.ORANGE)
    shapeRederer.rect(0, 64, Gdx.graphics.getWidth, 8)
    shapeRederer.end

  }

}
