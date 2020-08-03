#version 150

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

// uniform variables - can be set at any time from java code
uniform mat4 transformationMatrix;

void main(void){

    gl_Position = transformationMatrix * vec4(position.xyz, 1.0);
    pass_textureCoords = textureCoords;

}