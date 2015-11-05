package gdx.scala.demo

import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils.ShaderProgram

class GdxScalaDemoGame extends ApplicationAdapter with ShaderCapabilities {
  var camera:OrthographicCamera = _
  var shader:ShaderProgram = _
  var mesh: Mesh = _

  var player = new Player()

  override def create() {
    camera = new OrthographicCamera()
    mesh = createMesh()
    shader = createMeshShader()
  }

  override def render() {
    Gdx.gl.glClearColor(90/255f, 90/255f, 90/255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    drawEntity(player)
    flush()
  }

  override def resize(width:Int, height:Int) {
    val aspectRatio =  width / height
    camera = new OrthographicCamera(2f * aspectRatio, 2f)
  }

  override def dispose() {
    mesh.dispose()
    shader.dispose()
  }
}
