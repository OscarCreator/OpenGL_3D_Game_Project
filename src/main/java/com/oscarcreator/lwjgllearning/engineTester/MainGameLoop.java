package com.oscarcreator.lwjgllearning.engineTester;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.entities.Entity;
import com.oscarcreator.lwjgllearning.entities.Light;
import com.oscarcreator.lwjgllearning.entities.Player;
import com.oscarcreator.lwjgllearning.guis.GuiRenderer;
import com.oscarcreator.lwjgllearning.guis.GuiTexture;
import com.oscarcreator.lwjgllearning.models.TexturedModel;
import com.oscarcreator.lwjgllearning.models.TexturedModelFactory;
import com.oscarcreator.lwjgllearning.renderEngine.DisplayManager;
import com.oscarcreator.lwjgllearning.renderEngine.Loader;
import com.oscarcreator.lwjgllearning.renderEngine.MasterRenderer;
import com.oscarcreator.lwjgllearning.terrains.Terrain;
import com.oscarcreator.lwjgllearning.textures.TerrainTexture;
import com.oscarcreator.lwjgllearning.textures.TerrainTexturePack;
import com.oscarcreator.lwjgllearning.toolbox.MousePicker;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {

		//Open up the display
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		MasterRenderer renderer = new MasterRenderer(loader);

		TexturedModelFactory texturedModelFactory = new TexturedModelFactory();


		TexturedModel playerTexturedModel = texturedModelFactory.getTexturedModel(loader, "person", "playerTexture");
		Player player = new Player(playerTexturedModel, new Vector3f(370,4.2f, -300),0,180,0,0.7f);

		Camera camera = new Camera(player);

		TexturedModel treeModel = texturedModelFactory.getTexturedModel(loader, "lowPolyTree");

		TexturedModel fernModel = texturedModelFactory.getTexturedModel(loader, "fern", "fern-textureatlas");
		fernModel.getTexture().setNumberOfRows(2);
		fernModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(true);

		TexturedModel grassModel = texturedModelFactory.getTexturedModel(loader, "grassModel", "grassTexture");
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);

		TexturedModel lampModel = texturedModelFactory.getTexturedModel(loader, "lamp");
		lampModel.getTexture().setUseFakeLighting(true);

		List<Entity> entityList = new ArrayList<>();

		entityList.add(new Entity(lampModel, new Vector3f(185,-4.7f, -293), 0,0,0,1));
		entityList.add(new Entity(lampModel, new Vector3f(370,4.2f, -300), 0,0,0,1));
		Entity lampEntity = new Entity(lampModel, new Vector3f(293,-6.8f  , -305), 0,0,0,1);
		entityList.add(lampEntity);


		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
		lights.add(new Light(new Vector3f(185,10, -293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370,17, -300), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		Light light = new Light(new Vector3f(293,7, -305), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(light);


		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));


		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture,
				rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture((loader.loadTexture("blendMap")));

		//TODO create collision detection with multiple terrains
		Terrain terrain1 = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");


		Random rand = new Random();

		for (int i = 0; i < 400; i++){
			int randomNum = rand.nextInt(3);
			float randX = rand.nextFloat() * 800f;
			float randZ = rand.nextFloat() * 800f - 800;
			float y = terrain1.getHeightOfTerrain(randX, randZ);
			switch (randomNum){
				case 0: entityList.add(new Entity(fernModel, rand.nextInt(4), new Vector3f(randX, y, randZ), 0,0,0,1));
					break;
				case 1: entityList.add(new Entity(grassModel, new Vector3f(randX, y, randZ), 0,0,0,1));
					break;
				case 2: entityList.add(new Entity(treeModel, new Vector3f(randX, y, randZ), 0,0,0,1));
					break;
			}
		}

		// GUI

		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture gui = new GuiTexture(
				loader.loadTexture("white"),
				new Vector2f(-0.8f, 0.8f),
				new Vector2f(0.05f,0.05f));
		guis.add(gui);
		GuiTexture gui2 = new GuiTexture(
				loader.loadTexture("heightmap"),
				new Vector2f(-0.6f, 0.8f),
				new Vector2f(0.1f,0.1f));
		guis.add(gui2);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain1);

		Vector3f temp = new Vector3f();

		final boolean mousePicking = true;

		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain1);

			if (mousePicking){
				picker.update();
				Vector3f terrainPoint  = picker.getCurrentTerrainPoint();
				if (terrainPoint != null){
					lampEntity.setPosition(terrainPoint);
					temp.x = terrainPoint.x;
					temp.y = terrainPoint.y + 15;
					temp.z = terrainPoint.z;

					light.setPosition(temp);
				}
			}


			renderer.processEntity(player);
			renderer.processTerrain(terrain1);
			for (Entity e : entityList){
				renderer.processEntity(e);
			}

			renderer.render(camera, lights);

			guiRenderer.render(guis);

			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		//Close display
		DisplayManager.closeDisplay();

	}
}
