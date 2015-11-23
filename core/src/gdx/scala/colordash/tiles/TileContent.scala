package gdx.scala.colordash.tiles

import gdx.scala.colordash.{Effects, EffectState}

trait TileContent {

}

object Brick extends TileContent {

}

object Spike extends TileContent {

}

class Activator extends TileContent {
  var effect: EffectState = Effects.None
}