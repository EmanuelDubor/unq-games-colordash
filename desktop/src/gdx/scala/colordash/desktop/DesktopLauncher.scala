package gdx.scala.colordash.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gdx.scala.colordash.ColorDashGame

object DesktopLauncher {
  def main(arg: Array[String]) {
    val config: LwjglApplicationConfiguration = new LwjglApplicationConfiguration
    new LwjglApplication(ColorDashGame, config)
  }
}