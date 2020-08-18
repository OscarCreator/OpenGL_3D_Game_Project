package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objconverter.ModelData;
import objconverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		ModelData personData = OBJFileLoader.loadOBJ("person");

		RawModel playerRaw = loader.loadToVAO(personData.getVertices(),
				personData.getTextureCoords(),
				personData.getNormals(),
				personData.getIndices());
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture"));
		TexturedModel playerTexturedModel = new TexturedModel(playerRaw, playerTexture);
		Player player = new Player(playerTexturedModel, new Vector3f(30,0,-50),0,180,0,0.7f);

		ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
		TexturedModel treeModel = new TexturedModel(
				loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(),
						treeData.getNormals(), treeData.getIndices()),
				new ModelTexture(loader.loadTexture("lowPolyTree")));

		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		TexturedModel fernModel = new TexturedModel(
				loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(),
						fernData.getNormals(), fernData.getIndices()),
				new ModelTexture(loader.loadTexture("fern")));
		fernModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(true);

		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		TexturedModel grassModel = new TexturedModel(
				loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(),
						grassData.getNormals(), grassData.getIndices()),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);

		List<Entity> entityList = new ArrayList<>();

		Random rand = new Random();

		for (int i = 0; i < 20; i++){
			int randomNum = rand.nextInt(3);
			float randX = rand.nextFloat() * 200f;
			float randZ = rand.nextFloat() * 400f - 200;
			switch (randomNum){
				case 0: entityList.add(new Entity(fernModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
				case 1: entityList.add(new Entity(grassModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
				case 2: entityList.add(new Entity(treeModel, new Vector3f(randX, 0, randZ), 0,0,0,1));
			}
		}


		Light light = new Light(new Vector3f(10,50, -20), new Vector3f(1,1,1));

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));


		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture,
				rTexture,
				gTexture,
				bTexture);

		TerrainTexture blendMap = new TerrainTexture((loader.loadTexture("blendMap")));

		Terrain terrain1 = new Terrain(0,-1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(0,0, loader, texturePack, blendMap);

		Camera camera = new Camera(player);

		MasterRenderer renderer = new MasterRenderer();


		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();

			renderer.processEntity(player);
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
