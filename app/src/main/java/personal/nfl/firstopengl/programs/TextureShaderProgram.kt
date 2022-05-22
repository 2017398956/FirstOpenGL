package personal.nfl.firstopengl.programs

import android.content.Context
import android.opengl.GLES20.*
import personal.nfl.firstopengl.R

class TextureShaderProgram : ShaderProgram {
    private val uMatrixLocation : Int
    private val uTextureUnitLocation:Int

    val aPositionLocation:Int
    val aTextureCoordinatesLocation:Int

    constructor(context: Context):super(context , R.raw.texture_vertex_shader , R.raw.texture_fragment_shader){
        uMatrixLocation = glGetUniformLocation(program , U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        aPositionLocation = glGetAttribLocation(program , A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(program , A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix:FloatArray , textureId:Int){
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation , 1 , false , matrix , 0)
        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D , textureId)
        // Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation , 0)
    }
}