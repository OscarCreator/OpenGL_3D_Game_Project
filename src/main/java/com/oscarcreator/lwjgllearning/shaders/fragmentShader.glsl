#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour[4];
uniform vec3 attenuation[4];

uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

//cel shading levels
const float levels = 3.0f;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++){
        //distance from fragment to light
        float distance = length(toLightVector[i]);
        //function which will dim the light the further it goes with the use of a polynom
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDot1 = dot(unitNormal, unitLightVector);
        //how bright the fragment should be. Value from 0 to 1
        float brightness = max(nDot1, 0.0);

        //TODO add boolean to turn on and off cel shading

        //cel shading for the brightness
        float brightnessLevel = floor(brightness * levels);
        brightness = brightnessLevel / levels;

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

        //cel shading for the dampedFactor
        // (cel shading for the specular lighting)
        float dampedFactorLevel = floor(dampedFactor * levels);
        dampedFactor = dampedFactorLevel / levels;

        vec3 diffuse = (brightness * lightColour[i]) / attFactor;
        totalDiffuse = totalDiffuse + diffuse;

        vec3 specular = (dampedFactor * reflectivity * lightColour[i]) / attFactor;
        totalSpecular = totalSpecular + specular;
    }
    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColour = texture(textureSampler, pass_textureCoords);
    if (textureColour.a < 0.5){
        discard;
    }

    //Output color on the screen
    //finds the color of the texture at the passed texturecoordinate
    //adds specular lighting to the final colour
    out_Color = vec4(totalDiffuse, 1) * textureColour + vec4(totalSpecular, 1.0);
    //mix two colours with the factor visibility
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}