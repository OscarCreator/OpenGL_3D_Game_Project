package com.oscarcreator.lwjgllearning.textures;

import java.nio.ByteBuffer;

/**
 * Created by Oscar on 2020-08-22.
 */
public class TextureData {

	private int width;
	private int height;
	private ByteBuffer buffer;

	public TextureData(ByteBuffer buffer, int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
}
