package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();


		RawModel rawModel = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);
		texturedModel.getTexture().setShineDamper(10);
		texturedModel.getTexture().setReflectivity(0);


		Entity entity = new Entity(
				texturedModel,
				new Vector3f(0, -4,-15),
				0,0,0,0.6f);

		Light light = new Light(new Vector3f(0,5, 0), new Vector3f(1,1,1));

		Terrain terrain1 = new Terrain(0,0, loader,
				new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(0,1, loader,
				new ModelTexture(loader.loadTexture("image")));

		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();


		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0,0.5f,0);
			camera.move();

			renderer.processTerrain(terrain1, terrain2);
			renderer.processEntity(entity);

			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		//Close display
		DisplayManager.closeDisplay();

	}
}
