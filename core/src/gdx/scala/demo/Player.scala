package gdx.scala.demo

import com.badlogic.gdx.graphics.Color


class Player extends Positionable with Colored {
  override val color = new Color(0xae3333ff)

  w = 40
  h = 40

}

class Brick extends Positionable with Colored {

}

trait Colored {
  val color:Color = Color.LIGHT_GRAY
}

trait Positionable {
  var x = 0
  var y = 0
  var w = 0
  var h = 0

  def collidesWith(other: Positionable): Boolean = {
    isPointInside(x,y, other)    ||
    isPointInside(x+w,y, other)  ||
    isPointInside(x,y+h, other)  ||
    isPointInside(x+w,y+h, other)
  }

  def isPointInside(x: Int, y: Int, positionable: Positionable): Boolean = {
    x >= positionable.x && x <= positionable.x + positionable.w && y >= positionable.y && y <= positionable.y + positionable.h
  }
}