package gdx.scala.demo

import com.badlogic.gdx.graphics.Color

trait SquaredEntity extends Positionable with Colored with Renderizable {
  w = 40
  h = 40

  def render(renderer: ShaderRenderer): Unit =
    renderer.drawSquare(x,y, w,h,color)
}

class Player extends SquaredEntity {
  override val color = new Color(0xb25656ff)
}

class Brick extends SquaredEntity {
  override val color = new Color(0x303030ff)
}

trait Renderizable {
  def render(renderer:ShaderRenderer)
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

  def rightOf(other:Positionable): Unit ={
    x = other.x+other.w+1
  }

  def leftOf(other:Positionable): Unit ={
    x = other.x-this.w
  }

}



