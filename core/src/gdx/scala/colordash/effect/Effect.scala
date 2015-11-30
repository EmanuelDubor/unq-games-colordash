package gdx.scala.colordash.effect

import gdx.scala.colordash.Constants._
import gdx.scala.colordash.entities.Player

trait Effect {
  def applyEffect(player: Player): Unit
}

object Effects {

  object LargeJump extends Effect {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(largeJumpX, largeJumpY)
    }
  }

  object SmallJump extends Effect {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(smallJumpX, smallJumpY)
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