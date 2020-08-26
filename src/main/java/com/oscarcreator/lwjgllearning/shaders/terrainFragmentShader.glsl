#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour[4];
uniform vec3 attenuation[4];

uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

uniform float celShading;
uniform float levels = 5.0f;


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
    //add all colours from all 4 com.oscarcreator.lwjgllearning.textures
    vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++){
        //distance from fragment to light
        float distance = length(toLightVector[i]);
        //function which will dim the light the further it goes with the use of at polynom
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

        vec3 unitLightVector = normalize(toLightVector[i]);

        float nDot1 = dot(unitNormal, unitLightVector);
        float brightness = max(nDot1, 0.0);

        if (celShading > 0.5){
            //cel shading for the brightness
            float brightnessLevel = floor(brightness * levels);
            brightness = brightnessLevel / levels;
        }

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

        if (celShading > 0.5){
            //cel shading for the dampedFactor
            // (cel shading for the specular lighting)
            float dampedFactorLevel = floor(dampedFactor * levels);
            dampedFactor = dampedFactorLevel / levels;
        }

        vec3 diffuse = (brightness * lightColour[i]) / attFactor;
        totalDiffuse = totalDiffuse + diffuse;

        vec3 specular = (dampedFactor * reflectivity * lightColour[i]) / attFactor;
        totalSpecular = totalSpecular + specular;
    }
    totalDiffuse = max(totalDiffuse, 0.2);

    //Output color on the screen
    //adds specular lighting to the final colour
    out_Color = vec4(totalDiffuse, 1) * totalColour + vec4(totalSpecular, 1.0);
    //mix two colours with the factor visibility
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}