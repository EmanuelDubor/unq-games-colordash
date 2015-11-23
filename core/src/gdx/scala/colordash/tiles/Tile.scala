package gdx.scala.colordash.tiles

import com.badlogic.gdx.math.Rectangle
import gdx.scala.colordash.{Pool, Poolable, Constants, TiledWorld}

class Tile(var x: Int = 0, var y: Int = 0) extends Poolable {
  val width: Float = Constants.tileWidth
  val height: Float = Constants.tileHeigth

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

}

object Tile extends Pool[Tile] {
  def newObject: Tile = new Tile()

  def apply(x: Int, y: Int): Tile = {
    val tile = obtain
    tile.x = x
    tile.y = y
    tile
  }

  def apply(r: Rectangle): Tile = apply(r.x.toInt, r.y.toInt)

}




