#version 150

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
//0 - 1, first map - second map
uniform float blendFactor;
uniform vec3 fogColour;

//inbetween these limits the skybox will fade from all fog to all sky
const float lowerLimit = 0.0;
const float upperLimit = 30.0;

const float levels = 5f;

void main(void){
    //sample colour at the fragment
    vec4 texture1 = texture(cubeMap, textureCoords);
    vec4 texture2 = texture(cubeMap2, textureCoords);
    //mix the colours from both the cubemaps
    vec4 finalColour = mix(texture1, texture2, blendFactor);

    //cel shading
    // brightness of fragment
    float amount = (finalColour.r + finalColour.g + finalColour.b) / 3.0;
    //cap brightness with n levels
    amount = floor(amount * levels) / levels;

    finalColour.rgb = amount * fogColour;


    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    //mix fogColour and finalColour depending on the factor
    out_Color = mix(vec4(fogColour, 1.0), finalColour, factor);
}
