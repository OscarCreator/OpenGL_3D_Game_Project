#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

    //colour of the blendmap at the current passed cordinate
    vec4 blendMapColour = texture(blendMap, pass_textureCoords);
    //colour closer to black will render more backgroundtexture
    float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
    //tiled coordinate (makes texture repeat on single terrain)
    vec2 tiledCoords = pass_textureCoords * 40;
    vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmount;
    vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;
    //add all colours from all 4 textures
    vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;


    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness * lightColour;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    //vector from light to vertex
    vec3 lightDirection = -unitLightVector;
    //reflect light with the normal of the vertex
    // this reflected light makes up specular lightning
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    //how close the reflectedLightDirection to the cameraDirection
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    //sets 0 if negative
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    //Output color on the screen
    //adds specular lighting to the final colour
    out_Color = vec4(diffuse, 1) * totalColour + vec4(finalSpecular, 1.0);
    //mix two colours with the factor visibility
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}