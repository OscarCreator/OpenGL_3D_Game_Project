package com.oscarcreator.lwjgllearning.renderEngine;

import com.oscarcreator.lwjgllearning.entities.Entity;
import com.oscarcreator.lwjgllearning.models.RawModel;
import com.oscarcreator.lwjgllearning.models.TexturedModel;
import com.oscarcreator.lwjgllearning.shaders.StaticShader;
import com.oscarcreator.lwjgllearning.textures.ModelTexture;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static com.oscarcreator.lwjgllearning.util.Constants.*;

/**
 * Created by Oscar on 2020-07-29.
 */
public class EntityRenderer {




	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;

		//Load up the projection matrix once to the shader.

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}



	public void render(Map<TexturedModel, List<Entity>> entities){
		for (TexturedModel model : entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity e : batch){
				prepareInstances(e);
				GL11.glDrawElements(GL11.GL_TRIANGLES,
						model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0
				);
			}
			unbindTexturedModel();
		}
	}

	public void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		//enables the vbo for both position and texture
		GL20.glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(NORMAL_VBO_LOCATION);


		// Texture

		ModelTexture texture = model.getTexture();
		if (texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightning(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		//load rows of the texture atlas
		shader.loadNumberOfRows(model.getTexture().getNumberOfRows());
		//activate texturebank 0 which sampler2D in the fragmentshader
		// uses by default
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//bind the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

	}

	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(NORMAL_VBO_LOCATION);

		GL30.glBindVertexArray(0);
	}

	private void prepareInstances(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(),
				entity.getRotX(),
				entity.getRotY(),
				entity.getRotZ(),
				entity.getScale());

		shader.loadTransformationMatrix(transformationMatrix);
		//load offset per entity because they can have different offsets (same obj but different com.oscarcreator.lwjgllearning.textures)
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}



}
