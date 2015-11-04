package gdx.scala.demo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.scala.demo.GdxScalaDemoGame;
import gdx.scala.demo.MeshTutorial1;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GdxScalaDemoGame(), config);
//		new LwjglApplication(new MeshTutorial1(), config);
	}
}
