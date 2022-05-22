// 由于是顶点着色器，所以默认的是 highp 级别的精度
// precision highp float ;
attribute vec4 a_Position;
// 用于设置渐变色
attribute vec4 a_Color;

uniform mat4 u_Matrix;
varying vec4 v_Color;

void main(){
    v_Color = a_Color;
    // 开启正交投影
    gl_Position = u_Matrix * a_Position;
//    gl_Position = a_Position;
    gl_PointSize = 10.0;
}