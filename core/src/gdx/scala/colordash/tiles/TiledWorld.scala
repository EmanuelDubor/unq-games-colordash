package gdx.scala.colordash.tiles

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TmxMapLoader}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.entities.Renderizable
import gdx.scala.colordash.utils.LifeCycle

object TiledWorld extends LifeCycle {
  var levelMap: TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _

  def create(): Unit = {
    levelMap = new TmxMapLoader().load(Constants.mapFile)
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, Constants.unitScale)
  }

  def render(renderizables: Iterable[Renderizable], camera: OrthographicCamera): Unit = {
    mapRenderer.setView(camera)
    val batch = mapRenderer.getBatch
    batch.begin()
    TileEffectMap.render(batch)
    batch.end()
    mapRenderer.render()
    batch.begin()
    renderizables.foreach(_.render(batch))
    batch.end()
  }

  def getCell(x: Int, y: Int): Option[Cell] = levelMap.getLayers.get("level").asInstanceOf[TiledMapTileLayer].getCell(x, y) match {
    case null => None
    case cell => Some(cell)
  }

  def dispose() = levelMap.dispose()

}
