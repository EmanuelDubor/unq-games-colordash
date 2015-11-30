package gdx.scala.colordash

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.gui.GUI
import gdx.scala.colordash.tiles.{TileEffectMap, TiledWorld}

object ColorDashGame extends ApplicationAdapter {
  var gameCamera: OrthographicCamera = _

  var players = List.empty[Player]
  var currentPlayer: Player = _
  var paused = false

  override def create() {
    gameCamera = new OrthographicCamera()

    tiles.TiledWorld.initialize()

    currentPlayer = new Player
    players = players.+:(currentPlayer)
    gameCamera.setToOrtho(false, Constants.viewportWidth, Constants.viewportHeigth)
    gameCamera.update
    Gdx.input.setInputProcessor(InputHandler)
  }

  override def render() {
    if (!paused) {
      Gdx.gl.glClearColor(110 / 255f, 110 / 255f, 110 / 255f, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

      gameCamera.update
      implicit val delta = Gdx.graphics.getDeltaTime
      currentPlayer.update

      gameCamera.position.x = currentPlayer.rect.x + Constants.gameCameraXOffset
      gameCamera.position.y = currentPlayer.rect.y + Constants.gameCameraYOffset

      TiledWorld.render(players, gameCamera)
    }
    GUI.render()
  }

  def newPlayer = {
    TileEffectMap.clear
    currentPlayer = new Player
    players = players.+:(currentPlayer)
  }

  def togglePause = {
    paused = !paused
  }

}
