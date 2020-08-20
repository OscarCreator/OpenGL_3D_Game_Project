#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
//vector from vertex to camera
out vec3 toCameraVector;
out float visibility;

// uniform variables - can be set at any time from java code
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLightning;

uniform float numberOfRows;
uniform vec2 offset;

const float density = 0.0035;
const float gradient = 5.0;

void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    //vertex position relative to camer
    vec4 positionRelativeToCam = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * positionRelativeToCam;
    pass_textureCoords = (textureCoords / numberOfRows) + offset;

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

    //distance to the vertex to the camera
    float distance = length(positionRelativeToCam.xyz);
    //calculating visibility using exponential gradient
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}