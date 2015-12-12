package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.utils.{Composite, LifeCycle}

import scala.collection.JavaConversions._

object SectionManager extends Composite[TiledMap] with LifeCycle {

  var startArea: TiledMap = _

  val sections = 1 to 3

  def create(): Unit = {
    val loader = new TmxMapLoader()
    startArea = loader.load(Constants.startArea + Constants.mapExtension)
    addComponent(startArea)
    sections.foreach { i =>
      val section = loader.load(Constants.sectionsPath + "section" + i.toString + Constants.mapExtension)
      addComponent(section)
    }
  }

  def dispose(): Unit = components.foreach(_.dispose())
}
