package gdx.scala.colordash.effects

import gdx.scala.colordash.Constants._
import gdx.scala.colordash.entities.Player

trait EffectState {
  def applyEffect(player: Player): Unit
}

object Effects {

  object Jump extends EffectState {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(jumpX, jumpY)
    }
  }

  object Dash extends EffectState {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(dashX, dashY)
    }
  }

  object None extends EffectState {
    override def applyEffect(player: Player): Unit = {}
  }

}







