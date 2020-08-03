package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Oscar on 2019-08-12.
 */

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
													 float rz, float scale){
		//Creates a matrix
		Matrix4f matrix = new Matrix4f();
		//Set the diagonal to onces
		matrix.setIdentity();

		//translate to the wanted position
		Matrix4f.translate(translation, matrix, matrix);

		//Rotate matrix
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);

		//Scale it
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

}
