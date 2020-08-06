package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oscar on 2019-08-23.
 */

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer;


	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();


	public MasterRenderer(){

		enableCulling();

		renderer = new Renderer(shader);
	}

	//Disables rendering of backfaces(inside the model)
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Renders the entities and light from the perspective of the camera
	 * */
	public void render(Light sun, Camera camera){
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);

		renderer.render(entities);

		//Stop is used to make sure "noone" else other than me renders with it.
		//If another render is also used it is good practice to remove the "old one"
		// before using the "new one"
		shader.stop();


		//Remove the entities from the previous render. If this is not used the entities will
		// be summed up over time. The rendering will slow down because more and more entities
		// will be rendered.
		entities.clear();
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

	/**
	 * Stops the shaderprogram, detaches and deletes both the fragment and vertexshader.
	 * */
	public void cleanUp(){
		shader.cleanUp();
	}

}
