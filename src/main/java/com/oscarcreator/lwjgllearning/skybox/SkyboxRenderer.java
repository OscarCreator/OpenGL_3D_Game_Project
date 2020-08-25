package com.oscarcreator.lwjgllearning.skybox;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.models.RawModel;
import com.oscarcreator.lwjgllearning.renderEngine.DisplayManager;
import com.oscarcreator.lwjgllearning.renderEngine.Loader;
import com.oscarcreator.lwjgllearning.util.Constants;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Oscar on 2020-08-22.
 */
public class SkyboxRenderer {

	private static final float SIZE = 500f;

	private static final float[] VERTICES = {
			-SIZE, SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, SIZE
	};

	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightright", "nightleft", "nighttop", "nightbottom", "nightback", "nightfront"};

	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;

	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		//load up cube map texture
		texture = loader.loadCubeMap(TEXTURE_FILES);
		//load up night cube map texture
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		//filters the cubemaps to remove seams
		GL11.glEnable(GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS);

		//initialize shader
		shader = new SkyboxShader();

		shader.start();
		shader.connectTextureUnits();
		//load up projection matrix to the shader
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();

	}

	public void render(Camera camera, float r, float g, float b) {
		GL11.glDepthMask(false);
		GL11.glDepthRange(0.99f, 1f);
		shader.start();
		//load up view matrix every frame
		shader.loadViewMatrix(camera);
		//load up sky colour
		shader.loadFogColour(r, g, b);
		//bind the vao of the cube
		GL30.glBindVertexArray(cube.getVaoID());
		//enable position vbo location
		GL20.glEnableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//bind textures to the texture units and load up the blend factor
		bindTextures();
		//render the cube
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		//disable vbo location
		GL20.glDisableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//unbind vao
		GL30.glBindVertexArray(0);
		//stop shader
		shader.stop();

		GL11.glDepthRange(0f, 1f);
		GL11.glDepthMask(true);

	}

	private void bindTextures() {
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= 24000;

		int texture1;
		int texture2;
		float blendFactor;

		if (time >= 0 && time < 5000) {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0) / (5000);

		} else if (time >= 5000 && time < 8000) {
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5000) / (8000 - 5000);

		} else if (time >= 8000 && time < 21000) {
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 8000) / (21000 - 8000);

		} else {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 21000) / (24000 - 21000);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}

}
