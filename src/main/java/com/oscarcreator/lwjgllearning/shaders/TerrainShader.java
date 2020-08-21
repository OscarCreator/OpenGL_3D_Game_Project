package com.oscarcreator.lwjgllearning.shaders;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.entities.Light;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static com.oscarcreator.lwjgllearning.util.Constants.*;

/**
 * Created by Oscar on 2019-12-17.
 */
public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/main/java/com/oscarcreator/lwjgllearning/shaders/terrainFragmentShader.glsl";


	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;


	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}


	@Override
	protected void bindAttributes() {
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
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++){
			location_lightPosition[i] = super.getUniformLocation(String.format("lightPosition[%d]", i));
			location_lightColour[i] = super.getUniformLocation(String.format("lightColour[%d]", i));
		}

	}

	/**
	 * Load up int to textureSampler unit to specify which texture is used.
	 * */
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}

	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}


	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}


	public void loadLights(List<Light> lights){
		for (int i = 0; i < ShaderProgram.MAX_LIGHTS; i++){
			//as long as there is more lights in the array, load them up
			if (i < lights.size()){
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColour[i], lights.get(i).getColour());
				//if there is less than MAX_LIGHTS then load up empty
			}else{
				super.loadVector3f(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector3f(location_lightColour[i], new Vector3f(0,0,0));

			}
		}
	}

	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadSkyColour(float red, float green, float blue) {
		super.loadVector3f(location_skyColour, new Vector3f(red, green, blue));
	}
}