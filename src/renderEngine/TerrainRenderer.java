package renderEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;

import static util.Constants.*;

/**
 * Created by Oscar on 2019-12-17.
 */
public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		//load up the projection matrix at first creation
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<Terrain> terrains){
		for (Terrain terrain : terrains){
			//Binds the model and the texture
			prepareTerrain(terrain);
			//Load model matrix
			loadModelMatrix(terrain);

			//render
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);

			//unbind model and texture
			unbindTexturedModel();
		}
	}

	/**
	 * Binds the model-vao and the model-vbo's. Binds the texture into the
	 *  sampler2D and loads the shine, reflectivity variables into the shader.
	 * */
	private void prepareTerrain(Terrain terrain){
		//A raw model does NOT contain a texture, but it contains the id
		// to the vao with all the values for the model
		//vao contains: normals, colors (from the obj file), vertex position
		RawModel rawModel = terrain.getModel();

		//Binding the vao which has all the data in vbo's
		GL30.glBindVertexArray(rawModel.getVaoID());
		//Bind all the vbo's in the current bound vao
		GL20.glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(NORMAL_VBO_LOCATION);

		ModelTexture texture = terrain.getTexture();
		//Loads the values shinedamper, reflectivity into the shader with
		// ex. glUniform1f() (Which takes a float)
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

		//sampler2d uses GL13.GL_TEXTURE0 texturebank as the default
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//bind the textureId to the "texturebank variable" in the shader
		// this will give the color of each fragment on the model
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());

	}

	/**
	 * Unbinds all the VBO locations that is currently bound
	 * Currently we use three VBO's in texturemodel vao:
	 *  position, texture, normals
	 * */
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(NORMAL_VBO_LOCATION);

		GL30.glBindVertexArray(0);
	}

	/**
	 * Loads the transformation matrix of the specified entity.
	 * */
	private void loadModelMatrix(Terrain terrain){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(
				terrain.getX(), 0, terrain.getZ()), 0,0,0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
