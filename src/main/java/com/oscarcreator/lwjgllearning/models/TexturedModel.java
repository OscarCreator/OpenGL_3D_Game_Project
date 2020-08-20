package com.oscarcreator.lwjgllearning.models;

import com.oscarcreator.lwjgllearning.textures.ModelTexture;

/**
 * Created by Oscar on 2019-08-06.
 */

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}
}
