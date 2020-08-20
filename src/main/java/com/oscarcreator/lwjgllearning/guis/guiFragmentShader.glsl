#version 150

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main() {

    out_Color = texture(guiTexture, textureCoords);
}
