package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

import static util.Constants.POSITION_VBO_LOCATION;
import static util.Constants.TEXTURE_VBO_LOCATION;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/shaders/fragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

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
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	/**
	 * Load a matrix to the transformationMatrix variable to the shader.
	 * */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
