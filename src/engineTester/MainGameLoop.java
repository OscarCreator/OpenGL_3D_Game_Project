package engineTester;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);



		RawModel rawModel = OBJLoader.loadObjModel("lowPolyTree", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("lowPolyTree"));
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);

		Entity entity = new Entity(
				texturedModel,
				new Vector3f(0, -10,-15),
				0,0,0,1);

		Camera camera = new Camera();


		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0,0,-0.1f);
			camera.move();

			//Prepare for rendering
			renderer.prepare();
			//"start"/use this program
			shader.start();

			shader.loadViewMatrix(camera);

			//render model
			renderer.render(entity, shader);
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
