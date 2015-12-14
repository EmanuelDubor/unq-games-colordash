package gdx.scala.colordash

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.colordash.Constants.CameraValues._
import gdx.scala.colordash.Constants.TextureValues._
import gdx.scala.colordash.effect.Effects
import gdx.scala.colordash.entities.Player
import gdx.scala.colordash.tiledWorld.TiledWorld
import gdx.scala.colordash.ui.GUI
import gdx.scala.colordash.utils.LifeCycle

object ColorDashGame extends ApplicationAdapter with LifeCycle {
  var gameCamera: OrthographicCamera = _

  var players = List.empty[Player]
  var currentPlayer: Player = _
  var paused = false
  var textures: Texture = _
  var playerTexture: TextureRegion = _

  override def create() {
    textures = new Texture(gameTextures)
    playerTexture = TextureRegion.split(textures, tileWidthPx, tileHeightPx)(0)(0)

    gameCamera = new OrthographicCamera()
    gameCamera.setToOrtho(false, viewportWidth, viewportHeigth)
    gameCamera.position.y = viewportHeigth / 2 + gameCameraYOffset

    Gdx.input.setInputProcessor(GUI)
    TiledWorld.create()
    GUI.create()
    Effects.create()

    currentPlayer = new Player(playerTexture)
    players = players.+:(currentPlayer)
  }

  override def render() {

    Gdx.gl.glClearColor(110 / 255f, 110 / 255f, 110 / 255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    implicit val delta = Gdx.graphics.getDeltaTime
    if (!paused) {
      currentPlayer.update
    }
    gameCamera.position.x = currentPlayer.rect.x + gameCameraXOffset
    gameCamera.update()

    TiledWorld.render(players, gameCamera)

    GUI.render()
    TiledWorld.continueLevel(currentPlayer)
  }

  def newPlayer() = {
    TiledWorld.clearEffects()
    currentPlayer = new Player(playerTexture)
    players = players.+:(currentPlayer)
  }

  def togglePause() = {
    paused = !paused
  }

  override def dispose(): Unit = {
    textures.dispose()
    TiledWorld.dispose()
    GUI.dispose()
    Effects.dispose()
  }
}
