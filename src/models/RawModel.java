package models;

import org.lwjgl.opengl.*;

/**
 * Contains the model-vaoID and the vertex count.
 *
 * */
public class RawModel {

	private int vaoID;
	private int vertexCount;

	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * The vao id is "connected" to all the model data. The data
	 * is stored in it's own vbo inside the vao. To be able to get
	 * the data from the vbo's you have to bind the vao with {@link GL30#glBindVertexArray(int)}.
	 * */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Returns the vertex count for the specific model. The vertex
	 * data is stored inside the
	 * */
	public int getVertexCount() {
		return vertexCount;
	}

}
