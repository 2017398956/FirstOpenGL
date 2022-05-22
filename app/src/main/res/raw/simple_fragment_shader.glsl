// lowp , mediump , highp
precision mediump float ;
// 用于 glUniform4f 直接设置颜色
uniform vec4 u_Color;
varying vec4 v_Color;

void main(){
    gl_FragColor = u_Color;
//    gl_FragColor = v_Color;
}
