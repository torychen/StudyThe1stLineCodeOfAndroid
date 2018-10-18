#extension GL_OES_EGL_image_external : require
precision highp float;

uniform samplerExternalOES uTexture;
//uniform sampler2D uTexture;
varying vec2 vCoordinate;

void main(){
   vec4 color=texture2D(uTexture,vCoordinate);
   gl_FragColor=color;
   //gl_FragColor = vec4(0.2, 1.0, 0.129, 0); 
}