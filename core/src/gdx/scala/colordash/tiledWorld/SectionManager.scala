package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.utils.{Composite, LifeCycle}

import scala.collection.JavaConversions._

object SectionManager extends Composite[TiledMap] with LifeCycle {
  var loader: TmxMapLoader = _
  var startArea: TiledMap = _

  val sections = 1 to 3

  def create(): Unit = {
    loader = new TmxMapLoader()
    startArea = loader.load(Constants.startArea + Constants.mapExtension)
    addComponent(startArea)
    sections.foreach { i =>
      val sectionName = "section" + i
      loadSection(sectionName)
    }
  }

  def loadSection(sectionName: String): TiledMap = {
    val section = loader.load(Constants.sectionsPath + sectionName + Constants.mapExtension)
    section.getProperties.put("sectionName", sectionName)
    addComponent(section)
    section
  }

  def getSection(sectionName: String): TiledMap = {
    components.find(_.getProperties.get("sectionName", classOf[String]) == sectionName) match {
      case Some(section) => section
      case None => loadSection(sectionName)
    }
  }

  def dispose(): Unit = components.foreach(_.dispose())
}
