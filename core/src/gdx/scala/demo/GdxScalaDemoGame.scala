package gdx.scala.demo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class GdxScalaDemoGame extends ApplicationAdapter {
  var squareMesh:Mesh
  var camera:OrthographicCamera
  var texture:Texture
  var spriteBatch:SpriteBatch

  override def create() {
    if (squareMesh == null) {
      squareMesh = new Mesh(true, 4, 4,
        new VertexAttribute(Usage.Position, 3, "a_position"),
        new VertexAttribute(Usage.ColorPacked, 4, "a_color"))


      squareMesh.setVertices(Array[Float](
        -0.5f, -0.5f, 0, Color.toFloatBits(128, 0, 0, 255),
        0.5f, -0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
        -0.5f, 0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
        0.5f, 0.5f, 0, Color.toFloatBits(255, 0, 0, 255) ))

      squareMesh.setIndices(Array[Short]( 0, 1, 2, 3))
    }

    texture = new Texture(Gdx.files.internal("data/badlogic.jpg"))
    spriteBatch = new SpriteBatch()
  }


//  override def render() {
//    //    handleInput();
//    camera.update()
//    spriteBatch.setProjectionMatrix(camera.combined)
//
//    Gdx.gl.glDepthMask(false);
//
//    //enable blending, for alpha
//    Gdx.gl.glEnable(GL20.GL_BLEND);
//    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
//
//    squareMesh.render(GL20.GL_TRIANGLE_STRIP, 0, 4)
//
//    spriteBatch.begin();
//    spriteBatch.draw(texture, 0, 0, 1, 1, 0, 0,
//      texture.getWidth(), texture.getHeight(), false, false)
//    spriteBatch.end()
//  }
//
//  override def resize(width:Int, height:Int) {
//    float aspectRatio = (float) width / (float) height;
//    camera = new OrthographicCamera(2f * aspectRatio, 2f);
//  }

}
