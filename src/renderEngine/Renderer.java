package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static util.Constants.POSITION_VBO_LOCATION;

/**
 * Created by Oscar on 2020-07-29.
 */
public class Renderer {

	public void prepare(){
		//The background color used for clearing
		GL11.glClearColor(0,1,0,1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

	}

	public void render(RawModel rawModel){
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		//render triangles with indices buffer
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
