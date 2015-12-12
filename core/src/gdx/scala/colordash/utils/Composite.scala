package gdx.scala.colordash.utils

trait Composite[T] {

  val components = new com.badlogic.gdx.utils.Array[T]

  def addComponent(comp: T): Unit = components.add(comp)

}
