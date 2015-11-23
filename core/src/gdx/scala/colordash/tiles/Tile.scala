package gdx.scala.colordash.tiles

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import gdx.scala.colordash.{Constants, TiledWorld}

import scala.collection.JavaConversions._

class Tile(var x: Int = 0, var y: Int = 0) extends Poolable {
  val width: Float = Constants.tileWidth
  val height: Float = Constants.tileHeigth

  def free = Tile.free(this)

  def reset = {
    x = 0
    y = 0
  }

  def tileUp = TiledWorld.getTile(x, y + height.toInt)

  def tileDown = TiledWorld.getTile(x, y - height.toInt)

  def tileRight = TiledWorld.getTile(x + width.toInt, y)

  def tileLeft = TiledWorld.getTile(x - width.toInt, y)

  def overlaps(r: Rectangle) =
    x < r.x + r.width &&
      x + width > r.x &&
      y < r.y + r.height &&
      y + height > r.y

  def overlaps(t: Tile): Boolean = overlaps(t.asRectangle)

  def asRectangle = new Rectangle(x, y, width, height)
}

object Tile extends Pool[Tile] {
  def newObject: Tile = new Tile()

  def apply(x: Int, y: Int):Tile = {
    val tile = obtain
    tile.x = x
    tile.y = y
    tile
  }

  def apply(r: Rectangle):Tile = apply(r.x.toInt, r.y.toInt)

}

trait Poolable {
  def free: Unit

  def reset: Unit
}

trait Pool[T <: Poolable] {
  var maxPoolSize = Integer.MAX_VALUE
  val initialCapacity = 16
  val freeObjects = new Array[T](false, initialCapacity)

  def newObject: T

  def obtain: T =
    if (freeObjects.isEmpty) {
      newObject
    } else {
      freeObjects.pop
    }

  def free(obj: T):Unit = {
    obj.reset
    if (freeObjects.size < maxPoolSize) {
      freeObjects.add(obj)
    }
  }

  def freeAll(collection: Iterable[T]) = collection.foreach(free)

  def free(opt: Option[T]):Unit = opt match {
    case Some(obj) => free(obj)
    case _ =>
  }

  def clear = freeObjects.clear

}
