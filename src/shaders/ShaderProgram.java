package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

	//Id of the shader program
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	//Floatbuffer for reuse when loading up a matrix.
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile, String fragmentFile){
		//Load both the vertexshader and fragmentshader from file.
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		//Program
		programID = GL20.glCreateProgram();
		//Attaching vertex and fragmentshader to the program
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		//Must bind attributes before linking the program
		// otherwise it does not work properly
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}

	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	public void start(){
		GL20.glUseProgram(programID);
	}

	public void stop(){
		GL20.glUseProgram(0);
	}

	/**
	 * Stops the shaderprogram, detaches and deletes both the fragment and vertexshader.
	 * */
	public void cleanUp(){
		stop();
		//detach shaders from the program
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		//Delete the shaders
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		//Delete program
		GL20.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	/**
	 * Load float to shader at any time.
	 * */
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}

	/**
	 * Load vector to shader at any time.
	 * */
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Load boolean to shader at any time.
	 * */
	protected void loadBoolean(int location, boolean value){
		GL20.glUniform1f(location, value ? 1 : 0);
	}

	/**
	 * Load matrix to shader at any time.
	 * */
	protected void loadMatrix(int location, Matrix4f matrix){
		//Store matrix into floatbuffer
		matrix.store(matrixBuffer);
		//Make it ready to be read from
		matrixBuffer.flip();
		//Load it up to the shaders
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	/**
	 * Load int to shader at any time.
	 * */
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}


	/**
	 * Loads the text file and compiles it as a shader program
	 * */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shaders!");
			System.exit(-1);
		}
		return shaderID;
	}

}