package com.oscarcreator.lwjgllearning.skybox;

import com.oscarcreator.lwjgllearning.entities.Camera;
import com.oscarcreator.lwjgllearning.shaders.ShaderProgram;
import com.oscarcreator.lwjgllearning.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Oscar on 2020-08-22.
 */
public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/com/oscarcreator/lwjgllearning/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/main/java/com/oscarcreator/lwjgllearning/skybox/skyboxFragmentShader.glsl";

	private int location_projectionMatrix;
	private int location_viewMatrix;

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
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
