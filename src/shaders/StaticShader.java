package shaders;

import org.lwjgl.util.vector.Matrix4f;

import static util.Constants.POSITION_VBO_LOCATION;
import static util.Constants.TEXTURE_VBO_LOCATION;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/shaders/fragmentShader.glsl";

	private int location_transformationMatrix;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		//"position" is the variablename in the shader program
		super.bindAttribute(POSITION_VBO_LOCATION, "position");
		super.bindAttribute(TEXTURE_VBO_LOCATION, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	/**
	 * Load a matrix to the transformationMatrix variable to the shader.
	 * */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}


}
