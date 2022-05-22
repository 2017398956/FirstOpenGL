package personal.nfl.firstopengl.programs

import android.content.Context
import android.opengl.GLES20
import androidx.annotation.RawRes
import personal.nfl.firstopengl.util.ShaderHelper
import personal.nfl.firstopengl.util.TextResourceReader

abstract class ShaderProgram {
    // Uniform constants
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"
    protected val U_COLOR = "u_Color"

    // Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

    // Shader program
    protected val program: Int

    constructor(
        context: Context,
        @RawRes vertexShaderResourceId: Int,
        @RawRes fragmentShaderResourceId: Int
    ) {
        program = ShaderHelper.linkProgram(
            ShaderHelper.compileVertexShader(
                TextResourceReader.readTextFileFromResource(
                    vertexShaderResourceId
                )
            ),
            ShaderHelper.compileFragmentShader(
                TextResourceReader.readTextFileFromResource(
                    fragmentShaderResourceId
                )
            )
        )
    }

    fun useProgram() {
        GLES20.glUseProgram(program)
    }
}