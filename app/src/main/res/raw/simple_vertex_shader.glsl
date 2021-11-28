// 由于是顶点着色器，所以默认的是 highp 级别的精度
// precision highp float ;
attribute vec4 a_Position;
uniform mat4 u_Matrix;

void main(){
    gl_Position = u_Matrix * a_Position;
//    gl_Position = a_Position;
    gl_PointSize = 10.0;
}