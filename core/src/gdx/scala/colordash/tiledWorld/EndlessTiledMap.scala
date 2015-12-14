package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TiledMapTileSets}
import com.badlogic.gdx.maps.{MapLayers, MapObjects, MapProperties}
import com.badlogic.gdx.utils.{Array, Disposable}
import gdx.scala.colordash.utils.Composite

import scala.collection.JavaConversions._

class EndlessTiledMap(val firstSection: TiledMap, val sectionWidth: Int, val sectionHeight: Int) extends Composite[TiledMap] {

  addComponent(firstSection)

  def width = components.size * sectionWidth

  def height = sectionHeight

  def contains(x: Int, y: Int): Boolean =
    !(x < 0 || width <= x || y < 0 || height <= y)

  def getCell(x: Int, y: Int, layer: String="level"): Option[Cell] = {
    if (contains(x, y)) {
      val index = x / sectionWidth
      components.get(index).getLayers.get(layer).asInstanceOf[OffsetTiledMapTileLayer].getCell(x, y) match {
        case null => None
        case cell => Some(cell)
      }
    } else {
      None
    }
  }

  def lastSection(): TiledMap = components.last

  override def addComponent(comp: TiledMap): Unit = {
    super.addComponent(new OffsetTiledMap(comp, width, 0))
  }
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

class OffsetTiledMap(sourceMap: TiledMap, offsetX: Int, offsetY: Int) extends TiledMap {
  val layers: MapLayers = offsetSourceLayers()

  private def offsetSourceLayers(): MapLayers = {
    val sourceLayers = sourceMap.getLayers
    val offsetedLayers = new MapLayers
    sourceLayers.foreach { layer =>
      offsetedLayers.add(new OffsetTiledMapTileLayer(layer.asInstanceOf[TiledMapTileLayer], offsetX, offsetY))
    }
    offsetedLayers
  }

  override def setOwnedResources(resources: Array[_ <: Disposable]): Unit = sourceMap.setOwnedResources(resources)

  override def getTileSets: TiledMapTileSets = sourceMap.getTileSets

  override def dispose(): Unit = sourceMap.dispose()

  override def getLayers: MapLayers = layers

  override def getProperties: MapProperties = sourceMap.getProperties
}

class OffsetTiledMapTileLayer(sourceLayer: TiledMapTileLayer, offsetX: Int, offsetY: Int)
  extends TiledMapTileLayer(
    offsetX + sourceLayer.getWidth,
    offsetY + sourceLayer.getHeight,
    sourceLayer.getTileWidth.toInt,
    sourceLayer.getTileHeight.toInt
  ) {

  private def isInOffset(x: Int, y: Int): Boolean = x < offsetX && y < offsetY

  override def getCell(x: Int, y: Int): Cell = {
    if (isInOffset(x, y)) {
      null
    } else {
      sourceLayer.getCell(x - offsetX, y - offsetY)
    }
  }

  override def setCell(x: Int, y: Int, cell: Cell): Unit = {
    if (!isInOffset(x, y)) {
      sourceLayer.setCell(x - offsetX, y - offsetY, cell)
    }
  }

  override def isVisible: Boolean = sourceLayer.isVisible

  override def getName: String = sourceLayer.getName

  override def setVisible(visible: Boolean): Unit = sourceLayer.setVisible(visible)

  override def setName(name: String): Unit = sourceLayer.setName(name)

  override def getObjects: MapObjects = sourceLayer.getObjects

  override def getOpacity: Float = sourceLayer.getOpacity

  override def getProperties: MapProperties = sourceLayer.getProperties

  override def setOpacity(opacity: Float): Unit = sourceLayer.setOpacity(opacity)
}
