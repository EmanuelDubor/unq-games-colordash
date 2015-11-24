package gdx.scala.colordash.tiles

import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.{EffectState, Effects, Pool, Poolable}

trait TileContent {
  def applyTo(player: Player): Unit
}

object Brick extends TileContent {
  override def applyTo(player: Player): Unit = {}
}

object Spike extends TileContent {
  override def applyTo(player: Player): Unit = {
    player.defeat
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