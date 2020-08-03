package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Oscar on 2019-08-12.
 */

public class Camera {

	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;

	private Vector3f direction = new Vector3f(1,0,0);

	public Camera(){

	}

	//15:00
	public void move(){

		//yaw is around z axis
		System.out.println("yaw:" + yaw);

		float xzLen1 = (float) Math.cos(Math.toRadians(pitch));
		direction.z = -xzLen1 * (float) Math.cos(Math.toRadians(yaw));
		direction.y = -(float) Math.sin(Math.toRadians(pitch));
		direction.x = -xzLen1 * (float) Math.sin(-Math.toRadians(yaw));

		//direction.normalise();

		//System.out.println("x,y,z: " + direction.x +","+ direction.y +","+ direction.z);
		direction.scale(0.4f);

		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			//position.y +- direction.y
			position.set(position.x + direction.x,
						position.y,
						position.z + direction.z);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.set(position.x - direction.x,
					position.y,
					position.z - direction.z);
		}

		float xzLen2 = (float) Math.cos(Math.toRadians(pitch));
		direction.z = -xzLen2 * (float) Math.cos(Math.toRadians(yaw + 90));
		direction.y = -(float) Math.sin(Math.toRadians(pitch));
		direction.x = -xzLen2 * (float) Math.sin(-Math.toRadians(yaw + 90));

		direction.scale(0.4f);

		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.set(position.x + direction.x,
					position.y,
					position.z + direction.z);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.set(position.x - direction.x,
					position.y,
					position.z - direction.z);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y += 0.4f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y -= 0.4f;
		}


		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
			pitch -= 0.4f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			pitch += 0.4f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			yaw -= 0.4f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			yaw += 0.4f;
		}
	}

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
