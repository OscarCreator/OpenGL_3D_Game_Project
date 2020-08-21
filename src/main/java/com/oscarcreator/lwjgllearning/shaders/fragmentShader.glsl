#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++){
        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDot1 = dot(unitNormal, unitLightVector);
        float brightness = max(nDot1, 0.0);
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
        totalDiffuse = totalDiffuse + brightness * lightColour[i];
        totalSpecular = totalSpecular + dampedFactor * reflectivity * lightColour[i];
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