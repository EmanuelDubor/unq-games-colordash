package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import gdx.scala.colordash.utils.Composite

import scala.collection.JavaConversions._

class EndlessTiledMap(val firstSection: TiledMap, val sectionWidth: Int, val sectionHeight: Int) extends Composite[TiledMap] {

  addComponent(firstSection)

  def width = components.size * sectionWidth

  def height = sectionHeight

  def contains(x: Int, y: Int): Boolean =
    !(x < 0 || width <= x || y < 0 || height <= y)

  def getCell(x: Int, y: Int, layer: String): Option[Cell] = {
    if (contains(x, y)) {
      val index = x / sectionWidth
      val offset = x % sectionWidth
      components.get(index).getLayers.get(layer).asInstanceOf[TiledMapTileLayer].getCell(offset, y) match {
        case null => None
        case cell => Some(cell)
      }
    } else {
      None
    }
  }

  def lastSection(): TiledMap = components.last

}

class OrthogonalEndlessTiledMapRenderer(levelMap: EndlessTiledMap, unitScale: Float, batch: Batch) {
  var camera: OrthographicCamera = _

  val renderer = new BatchFixedOrthogonalTiledMapRenderer(levelMap.lastSection(), unitScale, batch)

  def setView(cam: OrthographicCamera) = {
    camera = cam
  }

  def render() = {
    levelMap.components.foreach { mapPiece =>
      renderer.setMap(mapPiece)
      renderer.setView(camera)
      renderer.render()
    }
  }

}

class BatchFixedOrthogonalTiledMapRenderer(levelMap: TiledMap, unitScale: Float, batch: Batch) extends OrthogonalTiledMapRenderer(levelMap, unitScale, batch) {

  override def beginRender(): Unit = {
    AnimatedTiledMapTile.updateAnimationBaseTime()
    if (ownsBatch) {
      batch.begin()
    }
  }

  override def endRender(): Unit = {
    if (ownsBatch) {
      batch.end()
    }
  }
}
