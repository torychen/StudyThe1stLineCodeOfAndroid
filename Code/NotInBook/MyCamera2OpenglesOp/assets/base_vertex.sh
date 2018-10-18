attribute vec4 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;

varying vec2 varyTexCoord;
varying vec2 varyPostion;

void main()
{
    gl_Position = vPosition; //vMatrix*vPosition
    varyPostion = vPosition.xy;
    varyTexCoord = vCoord;
}