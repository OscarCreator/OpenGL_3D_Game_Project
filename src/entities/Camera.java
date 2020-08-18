package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Oscar on 2019-08-12.
 */

public class Camera {

	private Vector3f position = new Vector3f(50,30,30);
	private float pitch = 20;
	private float yaw;
	private float roll;

	private Vector3f direction = new Vector3f(1,0,0);

	public Camera(){}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
