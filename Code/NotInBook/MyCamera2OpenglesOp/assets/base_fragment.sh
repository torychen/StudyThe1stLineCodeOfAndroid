precision mediump float;
uniform sampler2D vTexture;
uniform sampler2D vTexture0;
uniform sampler2D vTexture1;
uniform  float mratio;

const vec2 leftBottom = vec2(-0.99, 0.79);
const vec2 rightTop = vec2(-0.70, 0.99);

varying vec2 varyTexCoord;
varying vec2 varyPostion;

void main() {
    if (varyPostion.x >= leftBottom.x && varyPostion.x <= rightTop.x 
     && varyPostion.y >= leftBottom.y && varyPostion.y <= rightTop.y) {
        vec2 tex0 = vec2((varyPostion.x-leftBottom.x)/(rightTop.x-leftBottom.x),
                     1.0-(varyPostion.y-leftBottom.y)/(rightTop.y-leftBottom.y));
        vec4 color = texture2D(vTexture1, tex0);
        //color2.a = 0.5;
        gl_FragColor = color*color.a + texture2D(vTexture, 1.0-varyTexCoord)*(1.0-color.a);
        //gl_FragColor = texture2D(vTexture, 1.0-varyTexCoord);
    }
    else {
       vec4 color1 = texture2D(vTexture, varyTexCoord);
       vec4 color2 = texture2D(vTexture0, varyTexCoord); //vec2(varyTexCoord.s/10, varyTexCoord.t/10));
       gl_FragColor = mix(color1, color2, mratio);
    }
}