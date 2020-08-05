#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 diffuse = brightness * lightColour;

    //Output color on the screen
    //finds the color of the texture at the passed texturecoordinate
    out_Color = vec4(diffuse, 1) * texture(textureSampler, pass_textureCoords);

}