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
				0.5f, 0.5f, 0f,
		};

		int[] indices = {
				0,1,3, //Top left triangle
				3,1,2 //Bottom right triangle
		};


		RawModel rawModel = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			//game logic
			//rendering
			renderer.render(rawModel);
			DisplayManager.updateDisplay();
		}
		loader.cleanUp();
		//Close display
		DisplayManager.closeDisplay();

	}
}
