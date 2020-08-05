package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import toolbox.Maths;

import static util.Constants.*;

/**
 * Created by Oscar on 2020-07-29.
 */
public class Renderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private Matrix4f projectionMatrix;

	public Renderer(StaticShader shader) {
		//Load up the projection matrix once to the shader.
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//The background color used for clearing
		GL11.glClearColor(0,1,0,1);

	}

	public void render(Entity entity, StaticShader shader){
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());

		//enables the vbo for both position and texture
		GL20.glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(NORMAL_VBO_LOCATION);

		//Create and load up the matrix for the specific entity.
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(),
				entity.getRotX(),
				entity.getRotY(),
				entity.getRotZ(),
				entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);

		//activate texturebank 0 which sampler2D in the fragmentshader
		// uses by default
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		//bind the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

		//render triangles with indices buffer
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(NORMAL_VBO_LOCATION);

		GL30.glBindVertexArray(0);
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
