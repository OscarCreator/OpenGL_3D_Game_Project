package com.oscarcreator.lwjgllearning.skybox;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.models.RawModel;
import com.oscarcreator.lwjgllearning.renderEngine.Loader;
import com.oscarcreator.lwjgllearning.util.Constants;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Oscar on 2020-08-22.
 */
public class SkyboxRenderer {

	private static final float SIZE = 500f;

	private static final float[] VERTICES = {
			-SIZE,  SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,

			-SIZE, -SIZE,  SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE,  SIZE,
			-SIZE, -SIZE,  SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE,  SIZE,
			-SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE, -SIZE,  SIZE,
			-SIZE, -SIZE,  SIZE,

			-SIZE,  SIZE, -SIZE,
			SIZE,  SIZE, -SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			-SIZE,  SIZE,  SIZE,
			-SIZE,  SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE,  SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE,  SIZE,
			SIZE, -SIZE,  SIZE
	};

	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

	private RawModel cube;
	private int texture;
	private SkyboxShader shader;

	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
		cube = loader.loadToVAO(VERTICES, 3);
		//load up cube map texture
		texture = loader.loadCubeMap(TEXTURE_FILES);
		//initialize shader
		shader = new SkyboxShader();

		shader.start();
		//load up projection matrix to the shader
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();

	}

	public void render(Camera camera){
		shader.start();
		//load up view matrix every frame
		shader.loadViewMatrix(camera);
		//bind the vao of the cube
		GL30.glBindVertexArray(cube.getVaoID());
		//enable position vbo location
		GL20.glEnableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//activate texture unit 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//bind cube map texture to activated texture unit
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		//render the cube
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		//disable vbo location
		GL20.glDisableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//unbind vao
		GL30.glBindVertexArray(0);
		//stop shader
		shader.stop();
	}

}
