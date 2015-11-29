package gdx.scala.colordash

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TmxMapLoader}
import gdx.scala.colordash.entities.Renderizable

object TiledWorld {
  var levelMap: TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _

  def initialize(): Unit = {
    levelMap = new TmxMapLoader().load(Constants.mapFile)
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, Constants.unitScale)
  }

  def render(renderizables: Iterable[Renderizable], camera: OrthographicCamera): Unit = {
    mapRenderer.setView(camera)
    mapRenderer.render()
    val batch = mapRenderer.getBatch
    batch.begin()
    renderizables.foreach(_.render(batch))
    batch.end()
  }

  def getCell(x: Int, y: Int): Option[Cell] = levelMap.getLayers.get("level").asInstanceOf[TiledMapTileLayer].getCell(x, y) match {
    case null => None
    case cell => Some(cell)
  }

}
