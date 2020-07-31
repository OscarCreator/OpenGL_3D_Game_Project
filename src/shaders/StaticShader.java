package shaders;

import static util.Constants.POSITION_VBO_LOCATION;
import static util.Constants.TEXTURE_VBO_LOCATION;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE =
			"src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE =
			"src/shaders/fragmentShader.glsl";


	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		//"position" is the variablename in the shader program
		super.bindAttribute(POSITION_VBO_LOCATION, "position");
		super.bindAttribute(TEXTURE_VBO_LOCATION, "textureCoords");
	}

}
