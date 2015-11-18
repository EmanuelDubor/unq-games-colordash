package gdx.scala.colordash

import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.Constants._

trait EffectState {
  def applyEffect(player: Player):Unit
}

object Effects {
  object EffectNone extends EffectState {
    def applyEffect(player: Player): Unit = {
      if (player.velocity.x > player.BASE_VELOCITY) {
        player.velocity.x += FRICTION
      } else if (0 < player.velocity.x && player.velocity.x < player.BASE_VELOCITY) {
        player.velocity.x = player.BASE_VELOCITY
      }
    }
  }

  object EffectFalling extends EffectState {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(0, GRAVITY)
    }
  }

  object EffectJump extends EffectState {
    def applyEffect(player: Player): Unit = {
      player.velocity.add(JUMP_X_IMPULSE, JUMP_Y_IMPULSE)
    }
  }
}







