package gdx.scala.colordash

import com.badlogic.gdx.utils.Array

import scala.collection.JavaConversions._

trait Pool[T <: Poolable] {
  var maxPoolSize = Integer.MAX_VALUE
  val initialCapacity = 16
  val freeObjects = new Array[T](false, initialCapacity)

  def newObject: T

  def obtain: T =
    if (freeObjects.isEmpty) {
      newObject
    } else {
      freeObjects.pop
    }

  def free(obj: T):Unit = {
    obj.reset
    if (freeObjects.size < maxPoolSize) {
      freeObjects.add(obj)
    }
  }

  def freeAll(collection: Iterable[T]) = collection.foreach(free)

  def clear = freeObjects.clear

}

trait Poolable {
  def reset: Unit
}
