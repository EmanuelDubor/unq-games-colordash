package gdx.scala.colordash

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TmxMapLoader}
import gdx.scala.colordash.entities.Renderizable
import gdx.scala.colordash.tiles.Tile

object TiledWorld {
  var levelMap: TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _

  def initialize(): Unit = {
    val unitScale = 1 / 64f

    levelMap = new TmxMapLoader().load("boxes.tmx")
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, unitScale)
  }

  def render(renderizable: Renderizable, camera: OrthographicCamera): Unit = {
    mapRenderer.setView(camera)
    mapRenderer.render()
    val batch = mapRenderer.getBatch
    batch.begin()
    renderizable.render(batch)
    batch.end()
  }

  def findTiles(startX: Int, startY: Int, endX: Int, endY: Int)(implicit tiles: com.badlogic.gdx.utils.Array[Tile]) {
    val layer: TiledMapTileLayer = levelMap.getLayers.get("level").asInstanceOf[TiledMapTileLayer]
    tiles.clear()
    for (x <- startX to endX; y <- startY to endY) {
      val cell: TiledMapTileLayer.Cell = layer.getCell(x, y)
      if (cell != null) {
        tiles.add(Tile(x, y))
      }
    }
  }

  def getTile(x: Int, y: Int): Option[Tile] = {
    val layer: TiledMapTileLayer = levelMap.getLayers.get("level").asInstanceOf[TiledMapTileLayer]
    val cell = layer.getCell(x, y)
    cell match {
      case null => None
      case _ => Some(Tile(x, y))
    }
  }

}
