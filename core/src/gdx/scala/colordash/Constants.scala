package gdx.scala.colordash

object Constants {
    object TileValues {
    val tileHeight = 1f
    val tileWidth = 1f
    val touchMargin = 0.1f
  }
    object SectionManagerValues {
    val sectionWidth = 32
    val sectionHeight = 16
    val mapExtension = ".tmx"
    val sectionsPath = "sections/"
    val sectionNameKey = "sectionName"
    val nextSectionKey = "nextSection"
    val prevSectionKey = "prevSection"
    val startTopKey = "startTop"
    val startBottomKey = "startBottom"
    val endTopKey = "endTop"
    val endBottomKey = "endBottom"
    val defaultSectionOrder = "bestPick"
    val startArea = "startArea"
    val endArea = "endArea"
  }
  object TextureValues {
    val tileHeightPx = 64
    val tileWidthPx = 64
    val gameTextures = "boxes_map.png"
    val guiBase = "gui_base.png"
    val buttons = "buttons.png"
  }
  object CameraValues {
    val unitScale = 1 / 64f
    val viewportWidth = 24f
    val viewportHeigth = 18f
    val gameCameraXOffset = TileValues.tileWidth * 8f
    val gameCameraYOffset = TileValues.tileHeight * -2.5f
  }
  object PhysicsValues {
    val gravity = 10f
    val friction = -8f
  }
  object PlayerValues {
    val initialVelocity = 4f
    val startX = 5f
    val startY = 8f
    val maxStuck = 15f
    val maxSpike = 3f
  }
  object EffectValues {
    val smallJumpX = 1f
    val smallJumpY = 8f
    val dashX = 4f
    val dashY = 4f
  }
}
