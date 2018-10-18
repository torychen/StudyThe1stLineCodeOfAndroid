attribute vec4 vPosition;
attribute vec4 vTexCoordinate;
uniform mat4 textureTransform;
uniform mat4 uProjMatrix;
uniform mat4 uProjMatrix0;
uniform int xyFlag;            
varying vec2 v_TexCoordinate;

void main () {
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
    //gl_Position = vPosition;
    if(xyFlag==0){
       gl_Position = vPosition;
    }else if(xyFlag==1){
       gl_Position = uProjMatrix*vPosition;
    }else if(xyFlag==2){
       gl_Position = uProjMatrix0*vPosition;
    }
}