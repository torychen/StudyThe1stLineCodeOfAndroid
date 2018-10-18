#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture;
uniform int colorFlag;
const highp float mWidth=640.0;
const highp float mHeight=480.0;
const highp vec3 W = vec3(0.299,0.587,0.114);
const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721); //������������ֵ���ҪΪ1������ֵ��������ɫ�İٷֱȣ��м�����ɫ��ֵ��70%�ı��ػ���Ч�����õ㡣
const lowp float saturation=0.7;
const highp float radius = 1.41;
const highp vec2  center = vec2(0.5, 0.5);
const highp float refractiveIndex=0.5;

vec2 blurCoordinates[24];

varying vec4 gPosition;
varying vec2 v_TexCoordinate;

float hardLight(float color)
{
    if(color <= 0.5)
    color = color * color * 2.0;
    else
    color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);
    return color;
}

void modifyColor(vec4 color){
    color.r=max(min(color.r,1.0),0.0);
    color.g=max(min(color.g,1.0),0.0);
    color.b=max(min(color.b,1.0),0.0);
    color.a=max(min(color.a,1.0),0.0);
}

void main(){
    if(colorFlag==0){
       gl_FragColor = texture2D(texture, v_TexCoordinate);
    }
    else if(colorFlag==7){
       //������������ֵ���ҪΪ1������ֵ��������ɫ�İٷֱȣ��м�����ɫ��ֵ��70%�ı��ػ���Ч�����õ㡣
       vec4 textureColor = texture2D(texture, v_TexCoordinate);
       float luminance = dot(textureColor.rgb, luminanceWeighting); //GLSL�еĵ�����㣬���Դ����ĵ����������������֡���˼�����Ҫ��������ɫ��Ϣ�����Ӧ������Ȩ����ˡ�Ȼ��ȡ�����е�����ֵ��ӵ�һ�����õ�������ص��к�����ֵ��
       vec3 greyScaleColor = vec3(luminance); 
       gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w); //��mix�����Ѽ���ĻҶ�ֵ����ʶ��������ɫ�͵õ��ı��Ͷ���Ϣ���������
    }
    else if(colorFlag==8){
       float aspectRatio = mWidth/mHeight;
       vec2 textureCoordinateToUse = vec2(v_TexCoordinate.x, (v_TexCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
        //��һ������ռ���Ҫ������Ļ��һ����λ���һ����λ����
       float distanceFromCenter = distance(center, textureCoordinateToUse); //center
        //�����ض����ص�������ε������ж�Զ��ʹ��GLSL�ڽ���distance()�������ù��ɶ��ɼ������������ͳ���Ƚ���������������ľ���
       float checkForPresenceWithinSphere = step(distanceFromCenter, radius); //����Ƭ���Ƿ��������ڡ�
       distanceFromCenter = distanceFromCenter / radius;  //��׼�������ĵľ��룬��������distanceFromCenter
       float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter); //ģ��һ����������Ҫ������ġ���ȡ��Ƕ��١�
       vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth)); //��һ��
       vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex); 
       //GLSL��refract()�����ԸղŴ��������ߺ������������㵱����ͨ����ʱ������һ���㿴������Ρ�

       gl_FragColor = texture2D(texture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere; //���������м�����Ҫ����ɫ��Ϣ��
    }
    else if(colorFlag==1){ //���˻Ҷ�ֵ��Ϊ�����ɫ��RGBֵ�������ͻ��ɺڰ��˾�
       vec4 color = texture2D(texture, v_TexCoordinate);
       float fGrayColor = (0.3*color.r + 0.59*color.g + 0.11*color.b); //  ��Ҷ�ֵ
       gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
    }
    else if(colorFlag==2){    //��ůɫ��
       vec4 color = texture2D(texture, v_TexCoordinate);
       vec4 deltaColor = color + vec4(0.1, 0.1, 0.0, 0.0); // ůɫ
       modifyColor(deltaColor);
       gl_FragColor=deltaColor;
    }
    else if(colorFlag==3){    //�������ȡ��������ȵ�
       vec4 color = texture2D(texture, v_TexCoordinate);
       vec4 deltaColor = color + vec3(0.0, 0.0, 0.1, 0.0); //vec4(0.006, 0.004, 0.002, 0.0); // blueɫ
       modifyColor(deltaColor);
       gl_FragColor=deltaColor;
    }  
    else if(colorFlag==4){   //�Ŵ�Ч��
       vec4 nColor=texture2D(texture, v_TexCoordinate);
       float uXY = mWidth/mHeight;
       vec3 vChangeColor = vec3(0.25, 0.25, 0.25); //nColor.rgb;//vec3(0.0, 0.0, 0.4); // MAGNɫ
       float dis = distance(vec2(gPosition.x, gPosition.y/uXY), vec2(vChangeColor.r, vChangeColor.g));
       if(dis < vChangeColor.b){
           nColor=texture2D(texture,vec2(v_TexCoordinate.x/2.0+0.25, v_TexCoordinate.y/2.0+0.25));
       }
       gl_FragColor=nColor;
    }
    else if(colorFlag==5){ //���Ƹ�˹ģ��������ģ��
       vec4 nColor=texture2D(texture, v_TexCoordinate);
       vec3 vChangeColor = vec3(0.25, 0.25, 0.25);
       //vec3 vChangeColor = vec3(0.1, 0.1, 0.0); // ůɫ
       //vec3 vChangeColor = vec3({0.0, 0.0, 0.1); // ��ɫ       
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.r,v_TexCoordinate.y-vChangeColor.r));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.r,v_TexCoordinate.y+vChangeColor.r));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.r,v_TexCoordinate.y-vChangeColor.r));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.r,v_TexCoordinate.y+vChangeColor.r));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.g,v_TexCoordinate.y-vChangeColor.g));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.g,v_TexCoordinate.y+vChangeColor.g));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.g,v_TexCoordinate.y-vChangeColor.g));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.g,v_TexCoordinate.y+vChangeColor.g));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.b,v_TexCoordinate.y-vChangeColor.b));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x-vChangeColor.b,v_TexCoordinate.y+vChangeColor.b));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.b,v_TexCoordinate.y-vChangeColor.b));
       nColor+=texture2D(texture,vec2(v_TexCoordinate.x+vChangeColor.b,v_TexCoordinate.y+vChangeColor.b));
       nColor/=13.0;
       gl_FragColor=nColor;
    }
    else if(colorFlag==6)
    {
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