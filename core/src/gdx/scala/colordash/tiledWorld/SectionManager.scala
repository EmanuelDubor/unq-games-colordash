package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import com.badlogic.gdx.utils.{Array, ObjectMap}
import gdx.scala.colordash.Constants
import gdx.scala.colordash.utils.{Composite, LifeCycle}

import scala.collection.JavaConversions._
import scala.util.Random

object SectionManager extends Composite[TiledMap] with LifeCycle {
  var loader: TmxMapLoader = _
  var sortedSections: ObjectMap[(String, String), Array[TiledMap]] = _

  def create(): Unit = {
    val fileResolver = new RelativeFileHandleResolver(Constants.sectionsPath)
    loader = new TmxMapLoader(fileResolver)
    val sections = fileResolver.listFiles(Constants.mapExtension)
    sortedSections = new ObjectMap[(String, String), Array[TiledMap]](sections.length)
    sections.foreach { file =>
      val sectionName = file.nameWithoutExtension()
      loadSection(sectionName)
    }
  }

  def getSection(sectionName: String): TiledMap = {
    components.find(_.getProperties.get(Constants.sectionNameKey, classOf[String]) == sectionName) match {
      case Some(section) => section
      case None => loadSection(sectionName)
    }
  }

  def continueSection(section: TiledMap): TiledMap = {
    section.getProperties.get(Constants.nextSectionKey, Constants.defaultSectionOrder, classOf[String]) match {
      case Constants.defaultSectionOrder => bestPick(section)
      case nextSectionName => getSection(nextSectionName)
    }
  }

  protected def bestPick(section: TiledMap): TiledMap = {
    val sectionProperties = section.getProperties
    val sectionKey = (sectionProperties.get(Constants.endTopKey, classOf[String]), sectionProperties.get(Constants.endBottomKey, classOf[String]))
    val defaultSection = new Array[TiledMap]()
    defaultSection.add(getSection(Constants.endArea))
    val matches = sortedSections.get(sectionKey, defaultSection)
    matches.get(Random.nextInt(matches.size))
  }

  protected def loadSection(sectionName: String): TiledMap = {
    val section = loader.load(sectionName + Constants.mapExtension)
    val sectionProperties = section.getProperties
    sectionProperties.put(Constants.sectionNameKey, sectionName)
    sectionProperties.get(Constants.prevSectionKey, Constants.defaultSectionOrder, classOf[String]) match {
      case Constants.defaultSectionOrder => sortSection(section)
      case _ =>
    }
    addComponent(section)
    section
  }

  protected def sortSection(section: TiledMap): Unit = {
    val sectionProperties = section.getProperties
    val sectionKey = (sectionProperties.get(Constants.startTopKey, classOf[String]), sectionProperties.get(Constants.startBottomKey, classOf[String]))
    val sectionCategory = sortedSections.get(sectionKey, new Array[TiledMap]())
    sectionCategory.add(section)
    sortedSections.put(sectionKey, sectionCategory)
  }

  def dispose(): Unit = components.foreach(_.dispose())
}

class RelativeFileHandleResolver(rootPath: String) extends InternalFileHandleResolver {
  override def resolve(fileName: String): FileHandle = super.resolve(rootPath + fileName)

  def listFiles(fileExtension: String) = super.resolve(rootPath).list(fileExtension)
}
