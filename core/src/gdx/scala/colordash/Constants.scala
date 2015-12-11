package gdx.scala.colordash

object Constants {
  val tileHeigth = 1f
  val tileWidth = 1f
  val touchMargin = 0.1f
  val mapFile = "boxes.tmx"
  val gameTextures = "boxes_map.png"
  val guiBase = "gui_base.png"
  val buttons = "buttons.png"

  val unitScale = 1 / 64f
  val viewportWidth = 24f
  val viewportHeigth = 18f
  val gameCameraXOffset = tileWidth * 8f
  val gameCameraYOffset = tileHeigth * -2.5f

  val gravity = 10f
  val friction = -8f

  val initialVelocity = 4f
  val startX = 1f
  val startY = 8f
  val maxStuck = 15f
  val maxSpike = 3f

  val largeJumpX = 0f
  val largeJumpY = 13f

  val smallJumpX = 1f
  val smallJumpY = 8f

  val dashX = 4f
  val dashY = 4f

}
