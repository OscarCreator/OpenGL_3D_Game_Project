package com.oscarcreator.lwjgllearning.guis;

import com.oscarcreator.lwjgllearning.models.RawModel;
import com.oscarcreator.lwjgllearning.renderEngine.Loader;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import com.oscarcreator.lwjgllearning.util.Constants;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

/**
 * Created by Oscar on 2020-08-20.
 */
public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;

	public GuiRenderer(Loader loader){
		//Rendering using triangle strips
		// first 3 vertices is one triangle, and then for each new vertex a new
		// triangle is created with the two previous vertices.
		float[] positions = {
				-1,  1,
				-1, -1,
				 1,  1,
				 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}

	public void render(List<GuiTexture> guis){
		shader.start();
		//Bind the quad to render all gui's to.
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//disabling depth testing to be able to render guis over each other
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//render
		for (GuiTexture gui: guis){
			//Activate texturebank which textures will by default be bound to
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			//bind texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			//draw quad
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		//disabling alpha blending
		GL11.glDisable(GL11.GL_BLEND);
		//enabling depth testing
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL20.glDisableVertexAttribArray(Constants.POSITION_VBO_LOCATION);
		//unbind vao
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp(){
		shader.cleanUp();
	}

}
