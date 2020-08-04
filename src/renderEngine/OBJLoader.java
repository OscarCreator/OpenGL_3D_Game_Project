package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 2019-08-21.
 */

public class OBJLoader {

	public static RawModel loadObjModel(String fileName, Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldn't load file!");
		}

		BufferedReader reader = new BufferedReader(fr);
		String line = null;

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try{

			while (true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")){
					//Create vertex from line and add it to the list
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
									Float.parseFloat(currentLine[3]));
					vertices.add(vertex);

				}else if (line.startsWith("vt ")){
					//Create texturecoordinate from line and add it to the list
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);

				}else if (line.startsWith("vn ")){
					//Create normal vector from line and att it to the list
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if (line.startsWith("f ")){
					//we have read it all the data
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null){
				//If line starts with f then process it.
				if (line.startsWith("f")) {
					String[] currentLine = line.split(" ");
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");

					processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
					processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
					processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				}
				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		//Convert vertices list to array
		int vertexPointer = 0;
		for (Vector3f vertex : vertices){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;

		}

		//Convert indices indices list to array
		for (int i = 0; i < indices.size(); i++){
			indicesArray[i] = indices.get(i);
		}
		return loader.loadToVAO(verticesArray, textureArray, indicesArray);

	}

	/**
	 * Put texture coordinate and normals at the right position in the array
	 * */
	private static void processVertex(String[] vertexData, List<Integer> indices,
									  List<Vector2f> textures,
									  List<Vector3f> normals,
									  float[] textureArray,
									  float[] normalsArray){
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);

		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;

	}

}
