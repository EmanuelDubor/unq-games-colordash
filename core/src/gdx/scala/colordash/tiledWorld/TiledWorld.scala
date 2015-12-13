package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.{Batch, SpriteBatch}
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.utils.ObjectMap
import gdx.scala.colordash.Constants
import gdx.scala.colordash.effect.{Effect, Effects}
import gdx.scala.colordash.entities.{Player, Renderizable}
import gdx.scala.colordash.tiles.Tile
import gdx.scala.colordash.utils.LifeCycle

import scala.collection.JavaConversions._

object TiledWorld extends TileEffectMap with LifeCycle {
  var levelMap: EndlessTiledMap = _
  var mapRenderer: OrthogonalEndlessTiledMapRenderer = _
  var batch: Batch = _

  def create(): Unit = {
    SectionManager.create()
    batch = new SpriteBatch()
    levelMap = new EndlessTiledMap(SectionManager.getSection(Constants.startArea), Constants.sectionWidth, Constants.sectionHeight)
    levelMap.addComponent(SectionManager.getSection("section2"))
    mapRenderer = new OrthogonalEndlessTiledMapRenderer(levelMap, Constants.unitScale, batch)
  }

  def continueLevel(player: Player): Unit = {
    if (levelMap.width - player.rect.x < Constants.sectionWidth * 2) {
      levelMap.addComponent(SectionManager.continueSection(levelMap.lastSection()))
    }
  }

  def render(renderizables: Iterable[Renderizable], camera: OrthographicCamera): Unit = {
    mapRenderer.setView(camera)
    batch.begin()
    renderEffects(batch)
    mapRenderer.render()
    renderizables.foreach(_.render(batch))
    batch.end()
  }

  def getCell(x: Int, y: Int): Option[Cell] = levelMap.getCell(x, y, "level")

  def dispose() = {
    SectionManager.dispose()
    batch.dispose()
  }

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