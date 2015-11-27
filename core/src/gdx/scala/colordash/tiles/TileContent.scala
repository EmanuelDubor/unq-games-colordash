package gdx.scala.colordash.tiles

import gdx.scala.colordash.effects.{EffectState, Effects}
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.{Pool, Poolable}

trait TileContent {
  def applyTo(player: Player): Unit = {}
}

class Brick extends TileContent {}

object Brick {
  private var instance: Option[Brick] = None

  def apply(): Brick = instance match {
    case Some(brick) => brick
    case None =>
      val brick = new Brick
      instance = Some(brick)
      brick
  }
}

class Spike extends TileContent {}

object Spike {
  private var instance: Option[Spike] = None

  def apply(): Spike = instance match {
    case Some(spike) => spike
    case None =>
      val spike = new Spike
      instance = Some(spike)
      spike
  }
}

class Activator extends TileContent with Poolable {
  var effect: EffectState = Effects.None

  override def reset: Unit = effect = Effects.None

  override def applyTo(player: Player): Unit = {
    effect.applyEffect(player)
  }
}

object Activator extends Pool[Activator] {
  override def newObject: Activator = new Activator

  def apply(effect: EffectState): Activator = {
    val activator = obtain
    activator.effect = effect
    activator
  }
}