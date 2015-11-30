package gdx.scala.colordash.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle

trait SquaredEntity extends Positionable with Renderizable {
   def render(batch: Batch): Unit = {
   }
 }

trait Renderizable {
  def render(spriteBatch: Batch)
}

trait Positionable {
  val rect = new Rectangle()

  def collidesWith(other: Positionable): Boolean = {
    rect.overlaps(other.rect)
  }

}
