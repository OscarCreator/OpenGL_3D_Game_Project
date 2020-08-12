#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;

void main(void){

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


    vec4 textureColour = texture(textureSampler, pass_textureCoords);
    if (textureColour.a < 0.5){
        discard;
    }

    //Output color on the screen
    //finds the color of the texture at the passed texturecoordinate
    //adds specular lighting to the final colour
    out_Color = vec4(diffuse, 1) * textureColour + vec4(finalSpecular, 1.0);

}