package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;

public class Loader {

	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();

	/**
	 * Loads all the data into each wanted position in location, store the id's and
	 * return the needed information to access all the data when needed.
	 * */
	public RawModel loadToVAO(float[] positions){
		//Binds the vao
		int vaoID = createVAO();

		//Store the position, color (from obj), normals into each separate vbo location
		storeDataInAttributeList(POSITION_VBO_LOCATION, 3, positions);

		//unbind the vao. This makes sure that we don't accidentally render using the wrong vao.
		unbindVAO();
		return new RawModel(vaoID, positions.length / 3);
	}

	/**
	 * Removes all vaos and vbos stored in memory.
	 * */
	public void cleanUp(){
		for (int vao : vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}

	/**
	 * Create empty vao and return the id
	 * */
	private int createVAO(){
		//Create empty vao
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		//Bind the vao
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		//Create a empty vbo
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		//Bind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		//Convert data into a floatbuffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//Store floatbuffer in the vbo
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//Store vbo in the vao
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize,
				GL11.GL_FLOAT, false, 0, 0);
		//Unbinds the current vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Unbind the current bound vao.
	 * */
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}


	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		//Prepare to be read from
		buffer.flip();
		return buffer;
	}
}

