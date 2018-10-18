#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture;
uniform int colorFlag;
varying vec2 v_TexCoordinate;
const highp float mWidth=640.0;
const highp float mHeight=480.0;
const highp vec3 W = vec3(0.299,0.587,0.114);
vec2 blurCoordinates[24];

float hardLight(float color)
{
    if(color <= 0.5)
    color = color * color * 2.0;
    else
    color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);
    return color;
}

void main(){
    if(colorFlag==0){
       gl_FragColor = texture2D(texture, v_TexCoordinate);
    }
    else if(colorFlag==1){
       vec4 color = texture2D(texture, v_TexCoordinate);
       //  求此颜色的灰度值
       float fGrayColor = (0.3*color.r + 0.59*color.g + 0.11*color.b);
       //将此灰度值作为输出颜色的RGB值，这样就会变成黑白滤镜
       gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
    }
    else if(colorFlag==2){
    float mul_x = 2.0 / mWidth;
    float mul_y = 2.0 / mHeight;
    float pParams = 0.0;
    vec2 pStepOffset = vec2(mul_x, mul_y);
    vec3 centralColor = texture2D(texture, v_TexCoordinate).rgb;
    
    blurCoordinates[0] = v_TexCoordinate.xy + pStepOffset * vec2(0.0, -10.0);
    blurCoordinates[1] = v_TexCoordinate.xy + pStepOffset * vec2(0.0, 10.0);
    blurCoordinates[2] = v_TexCoordinate.xy + pStepOffset * vec2(-10.0, 0.0);
    blurCoordinates[3] = v_TexCoordinate.xy + pStepOffset * vec2(10.0, 0.0);
    blurCoordinates[4] = v_TexCoordinate.xy + pStepOffset * vec2(5.0, -8.0);
    blurCoordinates[5] = v_TexCoordinate.xy + pStepOffset * vec2(5.0, 8.0);
    blurCoordinates[6] = v_TexCoordinate.xy + pStepOffset * vec2(-5.0, 8.0);
    blurCoordinates[7] = v_TexCoordinate.xy + pStepOffset * vec2(-5.0, -8.0);
    blurCoordinates[8] = v_TexCoordinate.xy + pStepOffset * vec2(8.0, -5.0);
    blurCoordinates[9] = v_TexCoordinate.xy + pStepOffset * vec2(8.0, 5.0);
    blurCoordinates[10] = v_TexCoordinate.xy + pStepOffset * vec2(-8.0, 5.0);
    blurCoordinates[11] = v_TexCoordinate.xy + pStepOffset * vec2(-8.0, -5.0);
    blurCoordinates[12] = v_TexCoordinate.xy + pStepOffset * vec2(0.0, -6.0);
    blurCoordinates[13] = v_TexCoordinate.xy + pStepOffset * vec2(0.0, 6.0);
    blurCoordinates[14] = v_TexCoordinate.xy + pStepOffset * vec2(6.0, 0.0);
    blurCoordinates[15] = v_TexCoordinate.xy + pStepOffset * vec2(-6.0, 0.0);
    blurCoordinates[16] = v_TexCoordinate.xy + pStepOffset * vec2(-4.0, -4.0);
    blurCoordinates[17] = v_TexCoordinate.xy + pStepOffset * vec2(-4.0, 4.0);
    blurCoordinates[18] = v_TexCoordinate.xy + pStepOffset * vec2(4.0, -4.0);
    blurCoordinates[19] = v_TexCoordinate.xy + pStepOffset * vec2(4.0, 4.0);
    blurCoordinates[20] = v_TexCoordinate.xy + pStepOffset * vec2(-2.0, -2.0);
    blurCoordinates[21] = v_TexCoordinate.xy + pStepOffset * vec2(-2.0, 2.0);
    blurCoordinates[22] = v_TexCoordinate.xy + pStepOffset * vec2(2.0, -2.0);
    blurCoordinates[23] = v_TexCoordinate.xy + pStepOffset * vec2(2.0, 2.0);

    float sampleColor = centralColor.g * 22.0;
    sampleColor += texture2D(texture, blurCoordinates[0]).g;    
    sampleColor += texture2D(texture, blurCoordinates[1]).g;    
    sampleColor += texture2D(texture, blurCoordinates[2]).g;    
    sampleColor += texture2D(texture, blurCoordinates[3]).g;    
    sampleColor += texture2D(texture, blurCoordinates[4]).g;    
    sampleColor += texture2D(texture, blurCoordinates[5]).g;    
    sampleColor += texture2D(texture, blurCoordinates[6]).g;    
    sampleColor += texture2D(texture, blurCoordinates[7]).g;    
    sampleColor += texture2D(texture, blurCoordinates[8]).g;    
    sampleColor += texture2D(texture, blurCoordinates[9]).g;    
    sampleColor += texture2D(texture, blurCoordinates[10]).g;    
    sampleColor += texture2D(texture, blurCoordinates[11]).g;    
    sampleColor += texture2D(texture, blurCoordinates[12]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[13]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[14]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[15]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[16]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[17]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[18]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[19]).g * 2.0;    
    sampleColor += texture2D(texture, blurCoordinates[20]).g * 3.0;    
    sampleColor += texture2D(texture, blurCoordinates[21]).g * 3.0;    
    sampleColor += texture2D(texture, blurCoordinates[22]).g * 3.0;    
    sampleColor += texture2D(texture, blurCoordinates[23]).g * 3.0;    

    sampleColor = sampleColor / 62.0;

    float highPass = centralColor.g - sampleColor + 0.5;

    for(int i = 0; i < 5;i++)
    {
        highPass = hardLight(highPass);
    }
    float luminance = dot(centralColor, W);

    float alpha = pow(luminance, pParams);

    vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;

    gl_FragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centralColor), alpha), 1.0);
    }
}