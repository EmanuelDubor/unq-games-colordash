package gdx.scala.colordash.tiles

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TmxMapLoader}
import com.badlogic.gdx.utils.ObjectMap
import gdx.scala.colordash.Constants
import gdx.scala.colordash.effect.{Effect, Effects}
import gdx.scala.colordash.entities.Renderizable
import gdx.scala.colordash.utils.LifeCycle

import scala.collection.JavaConversions._

object TiledWorld extends TileEffectMap with LifeCycle {
  var levelMap: TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _

  def create(): Unit = {
    levelMap = new TmxMapLoader().load(Constants.sectionsPath + "section1" + Constants.mapExtension)
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, Constants.unitScale)
  }

  def render(renderizables: Iterable[Renderizable], camera: OrthographicCamera): Unit = {
    mapRenderer.setView(camera)
    val batch = mapRenderer.getBatch
    batch.begin()
    renderEffects(batch)
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

trait TileEffectMap {
  private val objectMap = new ObjectMap[(Int, Int), Effect]()

  def getEffect(tile: Tile): Effect = objectMap.get((tile.x, tile.y), Effects.None)

  def putEffect(tile: Tile, effect: Effect): Effect = objectMap.put((tile.x, tile.y), effect)

  def clearEffects() = objectMap.clear()

  def renderEffects(batch: Batch) = {
    objectMap.foreach { entry =>
      entry.value.render(batch, entry.key._1, entry.key._2)
    }
  }
}
