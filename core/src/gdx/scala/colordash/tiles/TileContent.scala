package gdx.scala.colordash.tiles

trait TileContent

class Activator extends TileContent

class Spike extends TileContent

class Brick extends TileContent

class Nothing extends TileContent

trait Singleton[T] {
  private var instance: Option[T] = None

  def newObject: T

  def apply(): T = instance match {
    case Some(obj) => obj
    case None =>
      val obj = newObject
      instance = Some(obj)
      obj
  }
}

object Activator extends Singleton[Activator] {
  override def newObject: Activator = new Activator
}

object Spike extends Singleton[Spike] {
  override def newObject: Spike = new Spike
}

object Brick extends Singleton[Brick] {
  def newObject: Brick = new Brick
}

object Nothing extends Singleton[Nothing] {
  override def newObject: Nothing = new Nothing
}
