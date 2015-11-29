package gdx.scala.colordash.effect

import gdx.scala.colordash.Constants._
import gdx.scala.colordash.entities.Player

trait Effect {
  def applyEffect(player: Player): Unit
}

object Effects {

  object Jump extends Effect {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(jumpX, jumpY)
    }
  }

  object Dash extends Effect {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(dashX, dashY)
    }
  }

  object None extends Effect {
    override def applyEffect(player: Player): Unit = {}
  }

}