package com.oscarcreator.lwjgllearning.renderEngine;

import com.oscarcreator.lwjgllearning.models.RawModel;
import com.oscarcreator.lwjgllearning.textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.oscarcreator.lwjgllearning.util.Constants.*;

public class Loader {

	//TODO change to a global path
	private static final String RES_LOC = "src/main/res/";

	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();

	/**
	 * Loads all the data into each wanted position in location, store the id's and
	 * return the needed information to access all the data when needed.
	 * */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices){
		//Binds the vao
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		//Store the position, color (from obj), normals into each separate vbo location
		storeDataInAttributeList(POSITION_VBO_LOCATION, 3, positions);
		//Store the texturecoordinates for the model
		storeDataInAttributeList(TEXTURE_VBO_LOCATION, 2, textureCoords);
		//Store the normals for the model
		storeDataInAttributeList(NORMAL_VBO_LOCATION, 3, normals);
		//unbind the vao. This makes sure that we don't accidentally render using the wrong vao.
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public RawModel loadToVAO(float[] positions, int dimensions){
		int vaoID = createVAO();
		this.storeDataInAttributeList(POSITION_VBO_LOCATION, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	/**
	 * Load up texture into memory and return the id for reference.
	 * */
	public int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("src/main/res/" + fileName + ".png"));
			//Generate all lower resolution images
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			//tell OpenGL to use render lower resolution when surface area is smaller to the camera.
			//LINEAR_MIPMAP_LINEAR = transition smoothly between different resolutions.
			//LINEAR_MIPMAP_NEAREST = transition is less smooth between different resolutions.
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			//LOD = level of detail, more negative means higher resolutions further away than usual
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}

	/**
	 * Loads 6 textures to  a cube map texture.
	 * The textures has to be in the order of
	 * 	Right, Left, Top, Bottom, Back, Front
	 * */
	public int loadCubeMap(String[] textureFiles){
		//generates a empty texture and returns it's id
		int texID = GL11.glGenTextures();
		//Bind activate texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//bind texture to texture0
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++){
			TextureData data = decodeTextureFile(RES_LOC + textureFiles[i] + ".png");

			int GL_TEXTURE_CUBE_MAP_INDEX = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i;
			GL11.glTexImage2D(GL_TEXTURE_CUBE_MAP_INDEX, 0, GL11.GL_RGBA,
					data.getWidth(), data.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}

		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);

		return texID;
	}

	private TextureData decodeTextureFile(String filename){
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(filename);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
			//ready to read
			buffer.flip();
			in.close();
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Tried to load texture " + filename + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
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
		for (int texture : textures){
			GL11.glDeleteTextures(texture);
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
		//Convert data into a floatbuffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//Bind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
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

	private void bindIndicesBuffer(int[] indices){
		//Create vbo.
		int vboID = GL15.glGenBuffers();
		//Add vbo to list.
		vbos.add(vboID);
		//Bind the vbo.
		//GL_ELEMENT_ARRAY_BUFFER tells OpenGL that it is the indices buffer.
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		//Convert the data to an intbuffer.
		IntBuffer buffer = storeDataInIntBuffer(indices);
		//store data in vbo.
		//GL_STATIC_DRAW tells OpenGL that we do not want to edit this data.
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		//Prepare to be read from
		buffer.flip();
		return buffer;
	}
}

