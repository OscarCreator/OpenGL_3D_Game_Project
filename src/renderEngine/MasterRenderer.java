package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

import java.util.*;

/**
 * Created by Oscar on 2019-08-23.
 */

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;

	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();


	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();


	private Matrix4f projectionMatrix;

	public MasterRenderer(){

		enableCulling();

		createProjectionMatrix();

		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	//Disables rendering of backfaces(inside the model)
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	/**
	 * Renders the entities and light from the perspective of the camera
	 * */
	public void render(Light sun, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);

		entityRenderer.render(entities);

		//Stop is used to make sure "noone" else other than me renders with it.
		//If another render is also used it is good practice to remove the "old one"
		// before using the "new one"
		shader.stop();

		//Load data
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);

		//render
		terrainRenderer.render(terrains);

		terrainShader.stop();

		//Remove the entities from the previous render. If this is not used the entities will
		// be summed up over time. The rendering will slow down because more and more entities
		// will be rendered.
		entities.clear();
		terrains.clear();
	}

	/**
	 * Adds the entity to the entitymap with the texture as the key and a
	 *  list of all the entities as value.
	 * */
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void processTerrain(Terrain... terrains){
		this.terrains.addAll(Arrays.asList(terrains));
	}


	/**
	 * Stops the shaderprogram, detaches and deletes both the fragment and vertexshader.
	 * */
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//The background color used for clearing
		//sky colour
		GL11.glClearColor(RED, GREEN, BLUE,1);

	}

	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1f;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
