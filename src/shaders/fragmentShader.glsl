#version 150

in vec2 pass_textureCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void){

    //Output color on the screen
    //finds the color of the texture at the passed texturecoordinate
    out_Color = texture(textureSampler, pass_textureCoords);

}