#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
//vector from vertex to camera
out vec3 toCameraVector;
out float visibility;

// uniform variables - can be set at any time from java code
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];

const float density = 0.0;
const float gradient = 5.0;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	//vertex position relative to camer
	vec4 positionRelativeToCam = viewMatrix * worldPosition;

	gl_Position = projectionMatrix * positionRelativeToCam;

	pass_textureCoords = textureCoords;

	//Normal vector of the vertex
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	for (int i = 0; i < 4; i++){
		//vector from vertex to light
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}

	//multiply by 0,0,0 to make the inverse matrix to camera position
	// then subtract the vertex worldposition to get the vector to the camera
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;


	//distance to the vertex to the camera
	float distance = length(positionRelativeToCam.xyz);
	//calculating visibility using exponential gradient
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}