#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture;
uniform int colorFlag;
varying vec2 v_TexCoordinate;

void main () {
    if(colorFlag==1){
       vec4 color = texture2D(texture, v_TexCoordinate);
       //求此颜色的灰度值
       float fGrayColor = (0.3*color.r + 0.59*color.g + 0.11*color.b);
       //将此灰度值作为输出颜色的RGB值，这样就会变成黑白滤镜
       gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
    }else{
       gl_FragColor = texture2D(texture, v_TexCoordinate);
    }
}