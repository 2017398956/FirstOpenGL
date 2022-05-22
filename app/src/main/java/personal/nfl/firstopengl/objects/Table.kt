package personal.nfl.firstopengl.objects

import android.opengl.GLES20
import personal.nfl.firstopengl.data.VertexArray
import personal.nfl.firstopengl.programs.TextureShaderProgram
import personal.nfl.firstopengl.util.Constants

class Table {
    private var vertexArray: VertexArray
    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
    private val STRIDE =
        (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT
    private val VERTEX_DATA = floatArrayOf(
        // x , y , s , t
        0f, 0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0f, 0.9f,
        0.5f, -0.8f, 1f, 0.9f,
        0.5f, 0.8f, 1f, 0.1f,
        -0.5f, 0.8f, 0f, 0.1f,
        -0.5f, -0.8f, 0f, 0.9f,
    )

    constructor() {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN , 0 , 6)
    }
}