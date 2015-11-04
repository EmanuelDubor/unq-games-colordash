package gdx.scala.demo

import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils.ShaderProgram

class GdxScalaDemoGame extends ApplicationAdapter with ShaderCapabilities {
  var camera:OrthographicCamera = _
  var shader:ShaderProgram = _
  var mesh: Mesh = _

  override def create() {
    camera = new OrthographicCamera()
    mesh = createMesh()
    shader = createMeshShader()
  }

  override def render() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    drawSquare(10, 10, 40, 40, Color.RED)
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
