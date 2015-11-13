package gdx.scala.colordash

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.colordash.entities.Player

class ColorDashGame extends ApplicationAdapter {
  var camera: OrthographicCamera = _

  var player: Player = _

  override def create() {
    camera = new OrthographicCamera()

    TiledWorld.initialize()

    player = new entities.Player()
    camera.setToOrtho(false, 20, 15)
    camera.update()
  }

  override def render() {
    Gdx.gl.glClearColor(110 / 255f, 110 / 255f, 110 / 255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update()
    player.update(Gdx.graphics.getDeltaTime)

    camera.position.x = player.rect.x
    camera.position.y = player.rect.y

    TiledWorld.render(player, camera)

  }

  override def resize(width: Int, height: Int) {
    //    val aspectRatio =  width / height
    //    camera = new OrthographicCamera(2f * aspectRatio, 2f)
  }

}
