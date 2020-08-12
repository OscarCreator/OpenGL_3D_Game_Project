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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();


		TexturedModel treeModel = new TexturedModel(
				OBJLoader.loadObjModel("lowPolyTree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel fermModel = new TexturedModel(
				OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		fermModel.getTexture().setHasTransparency(true);
		fermModel.getTexture().setUseFakeLighting(true);
		TexturedModel grassModel = new TexturedModel(
				OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);

		List<Entity> entityList = new ArrayList<>();

		Random rand = new Random();

		for (int i = 0; i < 25; i++){
			int randomNum = rand.nextInt(3);
			float randX = rand.nextFloat() * 100f;
			float randZ = rand.nextFloat() * 100f - 100;
			switch (randomNum){
				case 0: entityList.add(new Entity(fermModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
				case 1: entityList.add(new Entity(grassModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
				case 2: entityList.add(new Entity(treeModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
			}
		}


		Light light = new Light(new Vector3f(10,15, -20), new Vector3f(1,1,1));

		Terrain terrain1 = new Terrain(0,-1, loader,
				new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(0,0, loader,
				new ModelTexture(loader.loadTexture("image")));

		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();


		while (!Display.isCloseRequested()) {
			camera.move();

			renderer.processTerrain(terrain1, terrain2);
			for (Entity e : entityList){
				renderer.processEntity(e);
			}

			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		//Close display
		DisplayManager.closeDisplay();

	}
}
