package com.oscarcreator.lwjgllearning.guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Oscar on 2020-08-20.
 */
public class GuiTexture {

	private int texture;
	/**
	 * Center of the texture
	 * */
	private Vector2f position;
	/**
	 * x, y size of the screen
	 * */
	private Vector2f scale;

	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
