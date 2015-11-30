package gdx.scala.colordash

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.gui.GUI
import gdx.scala.colordash.tiles.{TileEffectMap, TiledWorld}

object ColorDashGame extends ApplicationAdapter {
  var gameCamera: OrthographicCamera = _

  var players = List.empty[Player]
  var currentPlayer: Player = _
  var paused = false
  var texture: Texture = _
  var playerTexture: TextureRegion = _

  override def create() {
    texture = new Texture(Constants.gameTextures)
    playerTexture = TextureRegion.split(texture, 64, 64)(0)(0)

    gameCamera = new OrthographicCamera()
    gameCamera.setToOrtho(false, Constants.viewportWidth, Constants.viewportHeigth)
    gameCamera.update

    Gdx.input.setInputProcessor(InputHandler)
    tiles.TiledWorld.initialize()
    GUI.create

    currentPlayer = new Player(playerTexture)
    players = players.+:(currentPlayer)
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
    GUI.render
  }

  def newPlayer = {
    TileEffectMap.clear
    currentPlayer = new Player(playerTexture)
    players = players.+:(currentPlayer)
  }

  def togglePause = {
    paused = !paused
  }

  override def dispose(): Unit = {
    texture.dispose
    TiledWorld.dispose
    GUI.dispose
  }
}
