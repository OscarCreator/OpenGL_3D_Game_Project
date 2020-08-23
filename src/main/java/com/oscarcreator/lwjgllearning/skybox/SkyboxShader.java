package com.oscarcreator.lwjgllearning.skybox;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.renderEngine.DisplayManager;
import com.oscarcreator.lwjgllearning.shaders.ShaderProgram;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Oscar on 2020-08-22.
 */
public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/com/oscarcreator/lwjgllearning/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/main/java/com/oscarcreator/lwjgllearning/skybox/skyboxFragmentShader.glsl";

	private static final float ROTATE_SPEED = 1f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColour;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;


	private float currentRotation = 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		//set translation to 0,0,0
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		//increase the rotation every frame
		// (movement of the sky)
		currentRotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		//rotate the matrix which will be applied in the shader
		Matrix4f.rotate((float) Math.toRadians(currentRotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColour = super.getUniformLocation("fogColour");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadFogColour(float r, float g, float b){
		super.loadVector3f(location_fogColour, new Vector3f(r, g, b));
	}

	public void loadBlendFactor(float blend){
		super.loadFloat(location_blendFactor, blend);
	}

	/**
	 * connects up the texture units with the cube map uniforms
	 * */
	public void connectTextureUnits(){
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

}
