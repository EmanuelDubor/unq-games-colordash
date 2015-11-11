package gdx.scala.demo

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.{TiledMapRenderer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.maps.tiled.renderers.{OrthogonalTiledMapRenderer, OrthoCachedTiledMapRenderer}
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils.ShaderProgram

class GdxScalaDemoGame extends ApplicationAdapter with ShaderRenderer {
  var camera:OrthographicCamera = _
  var shader:ShaderProgram = _
  var mesh: Mesh = _

  var player:Player = _
  var levelMap:TiledMap = _
  var mapRenderer: OrthogonalTiledMapRenderer = _

  override def create() {
    camera = new OrthographicCamera()
    mesh = createMesh()
    shader = createMeshShader()

    val unitScale = 1 / 64f

    player = new Player()
    levelMap = new TmxMapLoader().load("boxes.tmx")
    mapRenderer = new OrthogonalTiledMapRenderer(levelMap, unitScale)
    camera.setToOrtho(false, 20,15)
    camera.update()
  }

  override def render() {
    Gdx.gl.glClearColor(110/255f, 110/255f, 110/255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update()
    player.update(Gdx.graphics.getDeltaTime)
    camera.position.x = player.rect.x
    camera.position.y = player.rect.y


    mapRenderer.setView(camera)
    mapRenderer.render()

    val batch = mapRenderer.getBatch
    batch.begin()
    player.render(batch)
    batch.end()
//    drawEntity(player)
//    drawEntity(brick)
//    drawEntity(brick2)
//    flush()
  }

  override def resize(width:Int, height:Int) {
//    val aspectRatio =  width / height
//    camera = new OrthographicCamera(2f * aspectRatio, 2f)
  }

  override def dispose() {
    mesh.dispose()
    shader.dispose()
  }
}
