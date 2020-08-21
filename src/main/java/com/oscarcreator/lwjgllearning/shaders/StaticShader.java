package com.oscarcreator.lwjgllearning.shaders;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.entities.Light;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static com.oscarcreator.lwjgllearning.util.Constants.*;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/fragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLightning;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_attenuation[];


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
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLightning = super.getUniformLocation("useFakeLightning");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++){
			location_lightPosition[i] = super.getUniformLocation(String.format("lightPosition[%d]", i));
			location_lightColour[i] = super.getUniformLocation(String.format("lightColour[%d]", i));
			location_attenuation[i] = super.getUniformLocation(String.format("attenuation[%d]", i));
		}

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

	public void loadLights(List<Light> lights){
		for (int i = 0; i < MAX_LIGHTS; i++){
			//as long as there is more lights in the array, load them up
			if (i < lights.size()){
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColour[i], lights.get(i).getColour());
				super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
			//if there is less than MAX_LIGHTS then load up empty
			}else{
				super.loadVector3f(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector3f(location_lightColour[i], new Vector3f(0,0,0));
				//1,0,0 because you can't divide by zero
				super.loadVector3f(location_attenuation[i], new Vector3f(1,0,0));

			}
		}
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
