package gdx.scala.demo


import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.{Rectangle, Vector2}
import com.badlogic.gdx.utils.Pool

trait SquaredEntity extends Positionable with Colored with Renderizable {
  def render(batch:Batch): Unit ={
  }
}

class Player extends SquaredEntity {
  var velocity = new Vector2(5,0)
  var effectState = EffectNone
  val playerTexture= TextureRegion.split(new Texture("boxes_map.png"), 64, 64)(0)(0)
  private var rectPool: Pool[Rectangle] = new Pool[Rectangle]() {
    protected def newObject: Rectangle = {
      new Rectangle
    }
  }
  private var tiles: Array[Rectangle] = new Array[Rectangle](20)

  override val color = new Color(0xb25656ff)

  rect.width  = 1f
  rect.height = 1f

  override def render(batch:Batch): Unit ={
    batch.draw(playerTexture, rect.x, rect.y, rect.width,rect.height)
  }

  def update(delta:Float): Unit ={
    rect.x += velocity.x * delta
    rect.y += velocity.y * delta
  }
}

abstract class EffectState {
  def applyEffect(player:Player)
}

object EffectNone extends EffectState{
  override def applyEffect(player: Player): Unit ={
    //Do nothing
  }
}

class Brick extends SquaredEntity {
  override val color = new Color(0x303030ff)
}

trait Renderizable {
  def render(spriteBatch: Batch)
}

trait Colored {
  val color:Color = Color.LIGHT_GRAY
}

trait Positionable {
  val rect = new Rectangle()

  def collidesWith(other: Positionable): Boolean = {
    rect.overlaps(other.rect)
  }


}



