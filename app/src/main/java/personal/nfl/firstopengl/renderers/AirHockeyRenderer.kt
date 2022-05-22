package personal.nfl.firstopengl.renderers

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import personal.nfl.firstopengl.R
import personal.nfl.firstopengl.util.LogUtil
import personal.nfl.firstopengl.util.MatrixHelper
import personal.nfl.firstopengl.util.ShaderHelper
import personal.nfl.firstopengl.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer : GLSurfaceView.Renderer {

    enum class RenderMode {
        U_COLOR, V_COLOR
    }

    enum class MatrixMode {
        NORMAL, PERSPECTIVE
    }

    companion object {
        private val renderMode = RenderMode.V_COLOR
        private val matrixMode = MatrixMode.PERSPECTIVE
        private val POSITION_COMPONENT_COUNT = when (renderMode) {
            RenderMode.U_COLOR -> {
                2
            }
            RenderMode.V_COLOR -> {
                when (matrixMode) {
                    MatrixMode.PERSPECTIVE -> {
                        2
                    }
                    MatrixMode.NORMAL -> {
                        4
                    }
                }
            }
        }
        private const val BYTES_PER_FLOAT = 4
        private const val U_COLOR = "u_Color"
        private const val A_COLOR = "a_Color"
        private const val COLOR_COMPONENT_COUNT = 3
        private val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
        private const val A_POSITION = "a_Position"
        private const val U_MATRIX = "u_Matrix"
    }

    private var tableVerticesWithTrianglesUCOLOR = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        // line
        -0.5f, 0f,
        0.5f, 0f,
        // points
        0f, -0.25f,
        0f, 0.25f
    )
    private var tableVerticesWithTrianglesMatrixNormal = floatArrayOf(
        // 绘制三角扇 x , y , z , w , r , g , b
        0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        // line
        -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
        0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
        // 点
        0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
        0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f
    )
    private var tableVerticesWithTriangles = floatArrayOf(
        // 绘制三角扇 x , y , r , g , b
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        // line
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,
        // 点
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )

    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(
            when (renderMode) {
                RenderMode.U_COLOR -> {
                    tableVerticesWithTrianglesUCOLOR.size
                }
                RenderMode.V_COLOR -> {
                    when (matrixMode) {
                        MatrixMode.NORMAL -> {
                            tableVerticesWithTrianglesMatrixNormal.size
                        }
                        MatrixMode.PERSPECTIVE -> {
                            tableVerticesWithTriangles.size
                        }
                    }
                }
            } * BYTES_PER_FLOAT
        ).order(ByteOrder.nativeOrder()).asFloatBuffer()

    private var aPositionLocation = 0

    // uColor 模式
    private var uColorPositionLocation = 0

    // vColor 模式
    private var aColorLocation = 0
    private var uMatrixLocation = 0
    private val projectionMatrix = FloatArray(16)

    private val modelMatrix = FloatArray(16)

    constructor() {
        vertexData.put(
            when (renderMode) {
                RenderMode.U_COLOR -> {
                    tableVerticesWithTrianglesUCOLOR
                }
                RenderMode.V_COLOR -> {
                    when (matrixMode) {
                        MatrixMode.NORMAL -> {
                            tableVerticesWithTrianglesMatrixNormal
                        }
                        MatrixMode.PERSPECTIVE -> {
                            tableVerticesWithTriangles
                        }
                    }
                }
            }
        )
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
        aPositionLocation = GLES20.glGetAttribLocation(programObjectId, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(programObjectId, U_MATRIX)
        when (renderMode) {
            RenderMode.U_COLOR -> {
                uColorPositionLocation = GLES20.glGetUniformLocation(programObjectId, U_COLOR)
                // 不适用 stride 的情况
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
            RenderMode.V_COLOR -> {
                aColorLocation = GLES20.glGetAttribLocation(programObjectId, A_COLOR)
                // STRIDE 会导致读取下一个节点时，从开始位置跳过 STRIDE 个字节
                vertexData.position(0)
                GLES20.glVertexAttribPointer(
                    aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                    false, STRIDE, vertexData
                )
                GLES20.glEnableVertexAttribArray(aPositionLocation)

                vertexData.position(POSITION_COMPONENT_COUNT)
                GLES20.glVertexAttribPointer(
                    aColorLocation,
                    COLOR_COMPONENT_COUNT,
                    GLES20.GL_FLOAT,
                    false,
                    STRIDE,
                    vertexData
                )
                GLES20.glEnableVertexAttribArray(aColorLocation)
            }
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        LogUtil.d("width: $width , height: $height")
        // FIXME：为 true 时是书上的方法，转换成后性能会提升？
        val useDeprecated = true
        if (useDeprecated) {
            val aspectRatio = if (width > height) {
                width.toFloat() / height
            } else {
                height.toFloat() / width
            }
            LogUtil.d("aspectRatio $aspectRatio")
            if (width > height) {
                // landscape
                Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
            } else {
                Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
            }
        } else {
            Matrix.orthoM(projectionMatrix, 0, 0f, width.toFloat(), 0f, height.toFloat(), -1f, 1f)
        }
        // 设置投影
        when (renderMode) {
            RenderMode.V_COLOR -> {
                when (matrixMode) {
                    MatrixMode.PERSPECTIVE -> {
                        // 生成透视除法矩阵，这里不会受上面的正交投影操作的影响
                        MatrixHelper.perspectiveM(
                            projectionMatrix,
                            45f,
                            width.toFloat() / height,
                            1f,
                            10f
                        )
                        // 将 modelMatrix 变为 单位矩阵
                        Matrix.setIdentityM(modelMatrix , 0)
                        // 沿 z 轴移动 -3 个单位
                        Matrix.translateM(modelMatrix , 0 , 0f , 0f , -3f)
                        // 沿 x 轴顺时针旋转 60 度
                        Matrix.rotateM(modelMatrix , 0 , -60f , 1f , 0f , 0f)
                        val temp = FloatArray(16)
                        // 将 透视矩阵 乘以 模型矩阵
                        Matrix.multiplyMM(temp , 0 , projectionMatrix , 0 , modelMatrix , 0)
                        // 将最终的结果复制到 透视矩阵 中
                        System.arraycopy(temp , 0 , projectionMatrix , 0 , temp.size)
                    }
                }
            }
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        when (renderMode) {
            RenderMode.U_COLOR -> {
                // 开启正交投影
                GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
                // 绘制桌子
                GLES20.glUniform4f(uColorPositionLocation, 1.0f, 1.0f, 1.0f, 1.0f)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)
                // 绘制分割线
                GLES20.glUniform4f(uColorPositionLocation, 1.0f, 0.0f, 0.0f, 1.0f)
                GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)
                // 绘制点
                GLES20.glUniform4f(uColorPositionLocation, 0.0f, 0.0f, 1.0f, 1.0f)
                GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
                // 绘制点
                GLES20.glUniform4f(uColorPositionLocation, 0.0f, 1.0f, 0f, 1.0f)
                GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
            }
            RenderMode.V_COLOR -> {
                // 开启正交投影
                GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
                // 绘制桌子
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
                // 绘制分割线
                GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)
                // 绘制点
                GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
                // 绘制点
                GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
            }
        }
    }
}