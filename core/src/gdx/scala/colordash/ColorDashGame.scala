package gdx.scala.colordash

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.colordash.entities.Player

object ColorDashGame extends ApplicationAdapter {
  var camera: OrthographicCamera = _

  var players = List.empty[Player]

  var currentPlayer: Player = _

  override def create() {
    camera = new OrthographicCamera()

    TiledWorld.initialize()

    currentPlayer = new Player
    players = players.+:(currentPlayer)
    camera.setToOrtho(false, 20, 15)
    camera.update()
  }

  override def render() {
    Gdx.gl.glClearColor(110 / 255f, 110 / 255f, 110 / 255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update()
    implicit val delta = Gdx.graphics.getDeltaTime
    currentPlayer.update

    camera.position.x = currentPlayer.rect.x + Constants.cameraXOffset
    camera.position.y = currentPlayer.rect.y + Constants.cameraYOffset

    TiledWorld.render(players, camera)

  }

  override def resize(width: Int, height: Int) {
    //    val aspectRatio =  width / height
    //    camera = new OrthographicCamera(2f * aspectRatio, 2f)
  }

  def newPlayer = {
    currentPlayer = new Player
    players = players.+:(currentPlayer)
  }

}
