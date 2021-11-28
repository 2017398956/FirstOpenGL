package personal.nfl.firstopengl.renderers

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import personal.nfl.firstopengl.R
import personal.nfl.firstopengl.util.LogUtil
import personal.nfl.firstopengl.util.ShaderHelper
import personal.nfl.firstopengl.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer : GLSurfaceView.Renderer {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val BYTES_PER_FLOAT = 4
        private const val U_COLOR = "u_Color"
        private const val A_POSITION = "a_Position"
        private const val U_MATRIX = "u_Matrix"
    }

    private var tableVerticesWithTriangles = floatArrayOf(
//        // Triangle 1
//        -0.5f, -0.5f,
//        0.5f, 0.5f,
//        -0.5f, 0.5f,
//        // Triangle 2
//        -0.5f, -0.5f,
//        0.5f, -0.5f,
//        0.5f, 0.5f,
        // 绘制三角扇
        0f, 0f,
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        -0.5f, -0.5f,
        // line
        - 0.5f, 0f,
        0.5f, 0f,
        // 点
        0f, -0.25f,
        0f, 0.25f
    )

    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()

    private var uColorPositionLocation = 0
    private var aPositionLocation = 0
    private var uMatrixLocation = 0
    private val projectionMatrix = FloatArray(16)

    constructor() {
        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        val vertexShaderString =
            TextResourceReader.readTextFileFromResource(R.raw.simple_vertex_shader)
        val fragmentShaderString =
            TextResourceReader.readTextFileFromResource(R.raw.simple_fragment_shader)
        val vertexShaderObjectId = ShaderHelper.compileVertexShader(vertexShaderString)
        val fragmentShaderObjectId = ShaderHelper.compileFragmentShader(fragmentShaderString)
        val programObjectId = ShaderHelper.linkProgram(vertexShaderObjectId, fragmentShaderObjectId)
        ShaderHelper.validateProgram(programObjectId)
        GLES20.glUseProgram(programObjectId)
        uColorPositionLocation = GLES20.glGetUniformLocation(programObjectId, U_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programObjectId, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(programObjectId, U_MATRIX)
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            0,
            vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        LogUtil.d("width: $width , height: $height")
        val aspectRatio = if (width > height) {
            width.toFloat() / height
        } else {
            height.toFloat() / width
        }
        LogUtil.d("aspectRatio $aspectRatio")
        if (width > height) {
            // landscape
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        }else{
            Matrix.orthoM(projectionMatrix , 0 , -1f , 1f , -aspectRatio , aspectRatio , -1f , 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniformMatrix4fv(uMatrixLocation , 1 , false , projectionMatrix , 0)
        // 绘制桌子
        GLES20.glUniform4f(uColorPositionLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
        // 绘制分割线
        GLES20.glUniform4f(uColorPositionLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        GLES20.glUniform4f(uColorPositionLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        GLES20.glUniform4f(uColorPositionLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }
}