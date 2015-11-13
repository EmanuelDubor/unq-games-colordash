package gdx.scala.colordash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.GdxRuntimeException

trait ShaderRenderer {
  val POSITION_COMPONENTS: Int = 2
  val COLOR_COMPONENTS: Int = 4
  val NUM_COMPONENTS: Int = POSITION_COMPONENTS + COLOR_COMPONENTS
  val MAX_TRIS: Int = 2
  val MAX_VERTS: Int = MAX_TRIS * 4

  var verts = new Array[Float](MAX_VERTS * NUM_COMPONENTS)
  var idx: Int = 0

  var shader: ShaderProgram
  var mesh: Mesh
  var camera: OrthographicCamera

  val VERT_SHADER =
    """
      |attribute vec2 a_position;
      |attribute vec4 a_color;
      |uniform mat4 u_projTrans;
      |varying vec4 vColor;
      |void main() {
      |    vColor = a_color;
      |    gl_Position =  u_projTrans * vec4(a_position.xy, 0.0, 1.0);
      |}
    """.stripMargin

  val FRAG_SHADER =
    """
      |#ifdef GL_ES
      |precision mediump float;
      |#endif
      |varying vec4 vColor;
      |void main() {
      |    gl_FragColor = vColor;
      |}
    """.stripMargin

  def createMeshShader(): ShaderProgram = {
    ShaderProgram.pedantic = false
    val shader: ShaderProgram = new ShaderProgram(VERT_SHADER, FRAG_SHADER)
    val log: String = shader.getLog
    if (!shader.isCompiled) throw new GdxRuntimeException(log)
    if (log != null && log.length != 0) System.out.println("Shader Log: " + log)
    shader
  }

  def createMesh(): Mesh = {
    new Mesh(true, MAX_VERTS, 0,
      new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
      new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"))
  }

  protected def flush() {
    if (idx == 0) return
    mesh.setVertices(verts)
    Gdx.gl.glDepthMask(false)
    Gdx.gl.glEnable(GL20.GL_BLEND)
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    val vertexCount: Int = idx / NUM_COMPONENTS
    camera.setToOrtho(false, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    shader.begin()
    shader.setUniformMatrix("u_projTrans", camera.combined)
    mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, vertexCount)
    shader.end()
    Gdx.gl.glDepthMask(true)
    idx = 0
  }

  private[colordash] def drawSquare(x: Float, y: Float, width: Float, height: Float, color: Color) {
    if (idx == verts.length) flush()

    verts(nextIndex) = x
    verts(nextIndex) = y
    verts(nextIndex) = color.r
    verts(nextIndex) = color.g
    verts(nextIndex) = color.b
    verts(nextIndex) = color.a

    verts(nextIndex) = x
    verts(nextIndex) = y + height
    verts(nextIndex) = color.r
    verts(nextIndex) = color.g
    verts(nextIndex) = color.b
    verts(nextIndex) = color.a

    verts(nextIndex) = x + width
    verts(nextIndex) = y
    verts(nextIndex) = color.r
    verts(nextIndex) = color.g
    verts(nextIndex) = color.b
    verts(nextIndex) = color.a

    verts(nextIndex) = x + width
    verts(nextIndex) = y + height
    verts(nextIndex) = color.r
    verts(nextIndex) = color.g
    verts(nextIndex) = color.b
    verts(nextIndex) = color.a
  }

  protected def nextIndex: Int = {
    idx += 1
    idx - 1
  }

  def drawEntity(entity:Renderizable): Unit = {
//    entity.render(this)
//    flush()
  }
}
