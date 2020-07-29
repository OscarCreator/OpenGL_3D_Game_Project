package engineTester;

import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();

		//Counter clockwise order
		float[] vertices = {
				//Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				//Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};

		RawModel rawModel = loader.loadToVAO(vertices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			//game logic
			//rendering
			renderer.render(rawModel);
			DisplayManager.updateDisplay();
		}
		//Close display
		DisplayManager.closeDisplay();
		loader.cleanUp();

	}
}
