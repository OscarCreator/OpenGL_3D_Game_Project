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

uniform float useFakeLightning;

void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoords = textureCoords;

    vec3 actualNormal = normal;
    //replace with normal facing straight up
    if (useFakeLightning > 0.5){
        actualNormal = vec3(0, 1, 0);
    }

    //Normal vector of the vertex
    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    //vector from vertex to light
    toLightVector = lightPosition - worldPosition.xyz;

    //multiply by 0,0,0 to make the inverse matrix to camera position
    // then subtract the vertex worldposition to get the vector to the camera
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}