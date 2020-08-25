package com.oscarcreator.lwjgllearning.models;

import com.oscarcreator.lwjgllearning.objconverter.ModelData;
import com.oscarcreator.lwjgllearning.objconverter.OBJFileLoader;
import com.oscarcreator.lwjgllearning.renderEngine.Loader;
import com.oscarcreator.lwjgllearning.textures.ModelTexture;

import java.util.Objects;

/**
 * Created by Oscar on 2020-08-25.
 */
public class TexturedModelFactory {

	public TexturedModel getTexturedModel(Loader loader, String objFileName, String textureFileName) {
		ModelData personData = OBJFileLoader.loadOBJ(objFileName);

		RawModel playerRaw = loader.loadToVAO(Objects.requireNonNull(personData).getVertices(),
				personData.getTextureCoords(),
				personData.getNormals(),
				personData.getIndices());
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture(textureFileName));
		return new TexturedModel(playerRaw, playerTexture);
	}

	public TexturedModel getTexturedModel(Loader loader, String objAndTextureName) {
		ModelData personData = OBJFileLoader.loadOBJ(objAndTextureName);

		RawModel playerRaw = loader.loadToVAO(Objects.requireNonNull(personData).getVertices(),
				personData.getTextureCoords(),
				personData.getNormals(),
				personData.getIndices());
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture(objAndTextureName));
		return new TexturedModel(playerRaw, playerTexture);
	}


}
