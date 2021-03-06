package com.oscarcreator.lwjgllearning.entities;

import com.oscarcreator.lwjgllearning.models.TexturedModel;
import com.oscarcreator.lwjgllearning.renderEngine.DisplayManager;
import com.oscarcreator.lwjgllearning.terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Oscar on 2020-08-18.
 */
public class Player extends Entity {

	private static final float RUN_SPEED = 60;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -80;
	private static final float JUMP_POWER = 30;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (Math.sin(Math.toRadians(getRotY())) * distance);
		float dz = (float) (Math.cos(Math.toRadians(getRotY())) * distance);
		super.increasePosition(dx, 0, dz);

		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight){
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}

	}

	private void jump(){
		if (!isInAir){
			upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
	}

}
