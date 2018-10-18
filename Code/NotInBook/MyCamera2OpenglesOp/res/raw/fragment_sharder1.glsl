#extension GL_OES_EGL_image_external : require
precision mediump float;
precision mediump int;
uniform samplerExternalOES texture;
uniform int colorFlag;
varying vec2 v_TexCoordinate;

void main () {
    //if(colorFlag==1){
    //   vec4 color = texture2D(texture, v_TexCoordinate);
    //求此颜色的灰度值
    //   float fGrayColor = (0.3*color.r + 0.59*color.g + 0.11*color.b);
    //将此灰度值作为输出颜色的RGB值，这样就会变成黑白滤镜
    //   gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
    //}else{
    //   gl_FragColor = texture2D(texture, v_TexCoordinate);
    //}
    
    vec4 color = texture2D(texture, v_TexCoordinate);
    float fr1 = 0.393 * color.r + 0.769 * color.g + 0.189 * color.b;  
    float fg1 = 0.349 * color.r + 0.686 * color.g + 0.168 * color.b;  
    float fb1 = 0.272 * color.r + 0.534 * color.g + 0.131 * color.b;  
    int r1 = fr1;
    int g1 = fg1;
    int b1 = fb1;
    if(r1 > 255) {  
       r1 = 255;  
    }  
    if(g1 > 255) {  
       g1 = 255;  
    }  
    if(b1 > 255) {  
       b1 = 255;  
    }
    gl_FragColor = vec4(r1, g1, b1, 1.0);   
    /*
     vec4 color = texture2D(texture, v_TexCoordinate);
     color.r = 255 - color.r;  
     color.g = 255 - color.g;  
     color.b = 255 - color.b;
     if(color.r > 255) {  
        color.r = 255;  
     }else if(color.r < 0) {  
        color.r = 0;  
     }
     if(color.g > 255) {  
         color.g = 255;  
     }else if(color.g < 0) {  
         color.g = 0;  
     }  
     if(color.b > 255) {  
         color.b = 255;  
     }else if (color.b < 0) {  
         color.b = 0;  
     } 
     gl_FragColor = vec4(color.r, color.g, color.b, 1.0);
     */
}