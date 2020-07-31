package renderEngine;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static util.Constants.POSITION_VBO_LOCATION;
import static util.Constants.TEXTURE_VBO_LOCATION;

/**
 * Created by Oscar on 2020-07-29.
 */
public class Renderer {

	public void prepare(){
		//The background color used for clearing
		GL11.glClearColor(0,1,0,1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

	}

	public void render(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		//enables the vbo for both position and texture
		GL20.glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glEnableVertexAttribArray(TEXTURE_VBO_LOCATION);
		//activate texturebank 0 which sampler2D in the fragmentshader
		// uses by default
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//bind the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		//render triangles with indices buffer
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(POSITION_VBO_LOCATION);
		GL20.glDisableVertexAttribArray(TEXTURE_VBO_LOCATION);
		GL30.glBindVertexArray(0);
	}
}
