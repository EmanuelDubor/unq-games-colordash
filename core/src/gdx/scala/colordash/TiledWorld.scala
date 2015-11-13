package gdx.scala.colordash

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer, TmxMapLoader}
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import gdx.scala.colordash.entities.Renderizable

object TiledWorld {

  val rectPool: Pool[Rectangle] = new Pool[Rectangle]() {
    protected def newObject: Rectangle = {
      new Rectangle
    }
  }

  var levelMap:TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _
  
  def initialize(): Unit ={
    val unitScale = 1 / 64f

    levelMap = new TmxMapLoader().load("boxes.tmx")
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, unitScale)
  }

  def render(renderizable:Renderizable, camera:OrthographicCamera): Unit ={
    mapRenderer.setView(camera)
    mapRenderer.render()
    val batch = mapRenderer.getBatch
    batch.begin()
    renderizable.render(batch)
    batch.end()
  }

  def findTiles(startX: Int, startY: Int, endX: Int, endY: Int, tiles: com.badlogic.gdx.utils.Array[Rectangle], layerName:String="bricks") {
    val layer: TiledMapTileLayer = levelMap.getLayers.get(layerName).asInstanceOf[TiledMapTileLayer]
    rectPool.freeAll(tiles)
    tiles.clear()
    for( x <- startX to endX; y <- startY to endY){
      val cell: TiledMapTileLayer.Cell = layer.getCell(x, y)
      if (cell != null) {
        val rect: Rectangle = rectPool.obtain
        rect.set(x, y, 1, 1)
        tiles.add(rect)
      }
    }
  }
}
