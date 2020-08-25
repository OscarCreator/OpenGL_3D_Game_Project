package com.oscarcreator.lwjgllearning.textures;

/**
 * Created by Oscar on 2019-08-06.
 */
public class ModelTexture {

	private int textureId;
	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;

	private int numberOfRows = 1;

	public ModelTexture(int id){
		this.textureId = id;
	}

	/**
	 * Returns the id of the texture.
	 * @return the texture id.
	 * */
	public int getID() {
		return textureId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * Sets the amount of light which is reflected from the texture. Where a value of 0 reflects no light.
	 * This is used to emulate the effect of specular lighting.
	 * @return the amount of light it reflects.
	 * */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets the amount of light which is reflected from the texture. Where a value of 0 reflects no light.
	 * This is used to emulate the effect of specular lighting.
	 * @param reflectivity the amount of light it reflects.
	 * */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * The effect of transparently is made by not rendering black pixels
	 * on the texture.
	 * @return whether this texture is set to use transparency.
	 * */
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * Does not render where the texture is black if set to true.
	 * @param hasTransparency not rendering black parts of the texture if set to true.
	 * */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * Returns whether this texture should be rendered using one normal
	 * which points straight up or to use the vertex normals.
	 * @return true if texture will be rendered with fake lightning
	 * */
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * Renders using one normal which points straight up.
	 * @param useFakeLighting whether to render using one normal or the vertex normals
	 * */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	/**
	 * Returns the rows/columns of the textureatlas.
	 * @return the number of rows/columns of the textureatlas
	 * */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * Used for textureatlases, where rows = columns.
	 * @param numberOfRows the number of rows/columns the used textureatlas has.
	 * */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
}
