package engineTester;

import models.TexturedModel;
import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();

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

		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0
		};

		RawModel rawModel = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);

		while (!Display.isCloseRequested()) {
			//Prepare for rendering
			renderer.prepare();
			//"start"/use this program
			shader.start();
			//render model
			renderer.render(texturedModel);
			//stop using this program
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		//Close display
		DisplayManager.closeDisplay();

	}
}
