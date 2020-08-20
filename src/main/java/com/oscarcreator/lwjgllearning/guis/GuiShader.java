package com.oscarcreator.lwjgllearning.guis;

import com.oscarcreator.lwjgllearning.shaders.ShaderProgram;
import com.oscarcreator.lwjgllearning.util.Constants;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Oscar on 2020-08-20.
 */
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/com/oscarcreator/lwjgllearning/guis/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/main/java/com/oscarcreator/lwjgllearning/guis/guiFragmentShader.glsl";

	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(Constants.POSITION_VBO_LOCATION, "position");
	}




}
