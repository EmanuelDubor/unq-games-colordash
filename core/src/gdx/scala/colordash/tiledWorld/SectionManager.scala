package gdx.scala.colordash.tiledWorld

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import com.badlogic.gdx.utils.{Array, ObjectMap}
import gdx.scala.colordash.Constants.SectionManagerValues._
import gdx.scala.colordash.utils.{Composite, LifeCycle}

import scala.collection.JavaConversions._
import scala.util.Random

object SectionManager extends Composite[TiledMap] with LifeCycle {
  var loader: TmxMapLoader = _
  var sortedSections: ObjectMap[(String, String), Array[TiledMap]] = _

  def create(): Unit = {
    val fileResolver = new RelativeFileHandleResolver(sectionsPath)
    loader = new TmxMapLoader(fileResolver)
    val sections = fileResolver.listFiles(mapExtension)
    sortedSections = new ObjectMap[(String, String), Array[TiledMap]](sections.length)
    sections.foreach { file =>
      val sectionName = file.nameWithoutExtension()
      loadSection(sectionName)
    }
  }

  def getSection(sectionName: String): TiledMap = {
    components.find(_.getProperties.get(sectionNameKey, classOf[String]) == sectionName) match {
      case Some(section) => section
      case None => loadSection(sectionName)
    }
  }

  def continueSection(section: TiledMap): TiledMap = {
    section.getProperties.get(nextSectionKey, defaultSectionOrder, classOf[String]) match {
      case `defaultSectionOrder` => bestPick(section)
      case nextSectionName => getSection(nextSectionName)
    }
  }

  protected def bestPick(section: TiledMap): TiledMap = {
    val sectionProperties = section.getProperties
    val sectionKey = (sectionProperties.get(endTopKey, classOf[String]), sectionProperties.get(endBottomKey, classOf[String]))
    val defaultSection = new Array[TiledMap]()
    defaultSection.add(getSection(endArea))
    val matches = sortedSections.get(sectionKey, defaultSection)
    matches.get(Random.nextInt(matches.size))
  }

  protected def loadSection(sectionName: String): TiledMap = {
    val section = loader.load(sectionName + mapExtension)
    val sectionProperties = section.getProperties
    sectionProperties.put(sectionNameKey, sectionName)
    sectionProperties.get(prevSectionKey, defaultSectionOrder, classOf[String]) match {
      case `defaultSectionOrder` => sortSection(section)
      case _ =>
    }
    addComponent(section)
    section
  }

  protected def sortSection(section: TiledMap): Unit = {
    val sectionProperties = section.getProperties
    val sectionKey = (sectionProperties.get(startTopKey, classOf[String]), sectionProperties.get(startBottomKey, classOf[String]))
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
