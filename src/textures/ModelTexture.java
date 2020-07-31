package textures;

/**
 * Created by Oscar on 2019-08-06.
 *
 * Contains the ID connected to the texture in memory (pointer),
 * shinedamper and reflectivity variables used in the shader
 */

public class ModelTexture {

	private int textureId;

	public ModelTexture(int id){
		this.textureId = id;
	}

	public int getID() {
		return textureId;
	}

}
