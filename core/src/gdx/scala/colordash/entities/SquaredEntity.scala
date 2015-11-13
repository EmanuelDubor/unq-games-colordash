package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle

trait SquaredEntity extends Positionable with Colored with Renderizable {
   def render(batch: Batch): Unit = {
   }
 }

trait Renderizable {
  def render(spriteBatch: Batch)
}

trait Colored {
  val color: Color = Color.LIGHT_GRAY
}

trait Positionable {
  val rect = new Rectangle()

  def collidesWith(other: Positionable): Boolean = {
    rect.overlaps(other.rect)
  }

}
