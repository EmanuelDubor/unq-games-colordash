package gdx.scala.colordash.tiles

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pool.Poolable
import gdx.scala.colordash.Constants
import gdx.scala.colordash.effect.Effect
import gdx.scala.colordash.tiledWorld.TiledWorld
import gdx.scala.colordash.tiles.TileContent._

class Tile(var x: Int = 0, var y: Int = 0) extends Poolable {
  val width: Float = Constants.tileWidth
  val height: Float = Constants.tileHeight

  var content: TileContent = Nothing

  def reset() = {
    x = 0
    y = 0
    content = Nothing
  }

  def tileUp = Tile(x, y + height.toInt)

  def tileDown = Tile(x, y - height.toInt)

  def tileRight = Tile(x + width.toInt, y)

  def tileLeft = Tile(x - width.toInt, y)

  def isUnder(otherTile: Tile) = y < otherTile.y

  def isAbove(otherTile: Tile) = otherTile.y < y

  def isRight(otherTile: Tile) = otherTile.x < x

  def isLeft(otherTile: Tile) = x < otherTile.x

  def overlaps(r: Rectangle) =
    x < r.x + r.width &&
      x + width > r.x &&
      y < r.y + r.height &&
      y + height > r.y

  def touches(r: Rectangle) = {
    val xMargin = Constants.tileWidth * Constants.touchMargin
    val yMargin = Constants.tileHeight * Constants.touchMargin
    val rect = new Rectangle(x - xMargin, y - yMargin, width + (2 * xMargin), height + (2 * yMargin))
    rect.overlaps(r)
  }

  def isSolid = has(Brick) || has(Activator)

  def has(tileContent: TileContent): Boolean = content.equals(tileContent)

  def effect_=(effect: Effect) = TiledWorld.putEffect(this, effect)

  def effect = TiledWorld.getEffect(this)
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
        case "activator" => Activator
        case "spike" => Spike
        case "brick" => Brick
        case _ => Nothing
      }
      case _ => Nothing
    }
  }

}






