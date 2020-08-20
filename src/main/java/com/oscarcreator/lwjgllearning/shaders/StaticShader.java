package com.oscarcreator.lwjgllearning.shaders;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.entities.Light;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static com.oscarcreator.lwjgllearning.util.Constants.*;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/fragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLightning;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;


	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		//"position" is the variablename in the shader program
		super.bindAttribute(POSITION_VBO_LOCATION, "position");
		super.bindAttribute(TEXTURE_VBO_LOCATION, "textureCoords");
		super.bindAttribute(NORMAL_VBO_LOCATION, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLightning = super.getUniformLocation("useFakeLightning");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
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

	public void loadLight(Light light){
		super.loadVector3f(location_lightPosition, light.getPosition());
		super.loadVector3f(location_lightColour, light.getColour());
	}

	public void loadShineVariables(float damper, float reflectivity){
		loadFloat(location_shineDamper, damper);
		loadFloat(location_reflectivity, reflectivity);
	}

	public void loadFakeLightning(boolean useFake){
		super.loadBoolean(location_useFakeLightning, useFake);
	}

	public void loadSkyColour(float r, float g, float b){
		super.loadVector3f(location_skyColour, new Vector3f(r, g, b));
	}

	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	public void loadOffset(float x, float y){
		super.loadVector2f(location_offset, new Vector2f(x, y));
	}

}
