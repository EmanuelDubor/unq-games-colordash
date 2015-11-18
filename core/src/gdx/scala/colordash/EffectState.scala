package gdx.scala.colordash

import gdx.scala.colordash.entities.Player

trait EffectState {
  def applyEffect(player: Player):Unit
}

object Effects {
  object EffectNone extends EffectState {
    def applyEffect(player: Player): Unit = {
      if (player.velocity.x > player.INITIAL_VELOCITY) {
        player.velocity.x -= 0.01f
      } else if (0 < player.velocity.x && player.velocity.x < player.INITIAL_VELOCITY) {
        player.velocity.x = player.INITIAL_VELOCITY
      }
    }
  }

  object EffectFalling extends EffectState {
    val GRAVITY = -0.5f

    def applyEffect(player: Player): Unit = {
      player.velocity.add(0, GRAVITY)
    }
  }

  object EffectJump extends EffectState {
    val JUMP_IMPULSE = 4

    override def applyEffect(player: Player): Unit = {
      player.velocity.add(1, JUMP_IMPULSE)
    }
  }
}







