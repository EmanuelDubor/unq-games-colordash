package gdx.scala.colordash

object Constants {
  val tileHeigth=1f
  val tileWidth=1f
  val marginFactor=0.1f
  val mapFile="boxes.tmx"
  val gameTextures="boxes_map.png"

  val unitScale= 1 / 64f
  val viewportWidth=20f
  val viewportHeigth=15f
  val gameCameraXOffset= 0f
  val gameCameraYOffset= tileHeigth * 2.5f

  val gravity = 10f
  val friction = -8f

  val initialVelocity = 4f
  val startX=1f
  val startY=8f

  val jumpX = 1f
  val jumpY = 8f

  val dashX = 0.5f
  val dashY = 0.4f

}
