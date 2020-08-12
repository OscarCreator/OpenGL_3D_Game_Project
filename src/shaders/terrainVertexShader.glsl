#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
//vector from vertex to camera
out vec3 toCameraVector;

// uniform variables - can be set at any time from java code
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	//the multiplier makes texturing one terrain with the same texture 10 time
	pass_textureCoords = textureCoords * 10;

	//Normal vector of the vertex
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	//vector from vertex to light
	toLightVector = lightPosition - worldPosition.xyz;

	//multiply by 0,0,0 to make the inverse matrix to camera position
	// then subtract the vertex worldposition to get the vector to the camera
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}