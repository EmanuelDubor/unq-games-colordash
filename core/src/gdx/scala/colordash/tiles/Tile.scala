package gdx.scala.colordash.tiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.{ObjectMap, Pool}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.effect.{Effect, Effects}

import scala.collection.JavaConversions._
import scala.reflect.ClassTag

class Tile(var x: Int = 0, var y: Int = 0) extends Poolable {
  val width: Float = Constants.tileWidth
  val height: Float = Constants.tileHeigth

  var content: TileContent = Nothing()

  def reset() = {
    x = 0
    y = 0
    content = Nothing()
  }

  def tileUp = Tile(x, y + height.toInt)

  def tileDown = Tile(x, y - height.toInt)

  def tileRight = Tile(x + width.toInt, y)

  def tileLeft = Tile(x - width.toInt, y)

  def overlaps(r: Rectangle) =
    x < r.x + r.width &&
      x + width > r.x &&
      y < r.y + r.height &&
      y + height > r.y

  def touches(r: Rectangle) = {
    val xMargin = Constants.tileWidth * Constants.marginFactor
    val yMargin = Constants.tileHeigth * Constants.marginFactor
    val rect = new Rectangle(x - xMargin, y - yMargin, width + (2 * xMargin), height + (2 * yMargin))
    rect.overlaps(r)
  }

  def has[T <: TileContent : ClassTag]: Boolean = {
    val klass = implicitly[ClassTag[T]].runtimeClass
    klass.isInstance(content)
  }

  def effect_=(effect: Effect) = TileEffectMap.put(this, effect)

  def effect = TileEffectMap.get(this)
}

object Tile extends Pool[Tile] {
  def newObject(): Tile = new Tile()

  def apply(x: Int, y: Int): Tile = {
    val tile = obtain
    tile.x = x
    tile.y = y
    tile.content = TiledWorld.getCell(x, y).content
    tile
  }

  def apply(r: Rectangle): Tile = apply(r.x.toInt, r.y.toInt)

  def findTiles(startX: Int, startY: Int, endX: Int, endY: Int)(implicit tiles: com.badlogic.gdx.utils.Array[Tile]) {
    tiles.clear()
    for (x <- startX to endX; y <- startY to endY) {
      tiles.add(Tile(x, y))
    }
  }

  implicit class CellParser(optionCell: Option[Cell]) {
    def content: TileContent = optionCell match {
      case Some(cell) => cell.getTile.getProperties.get("type") match {
        case "activator" => Activator()
        case "spike" => Spike()
        case "brick" => Brick()
        case _ => Nothing()
      }
      case _ => Nothing()
    }
  }

}

object TileEffectMap extends ObjectMap[(Int, Int), Effect] {
  def get(tile: Tile): Effect = get((tile.x, tile.y), Effects.None)

  def put(tile: Tile, effect: Effect): Effect = put((tile.x, tile.y), effect)

  def render(batch: Batch) = {
    this.foreach { entry =>
      entry.value.render(batch, entry.key._1, entry.key._2)
    }
  }
}




