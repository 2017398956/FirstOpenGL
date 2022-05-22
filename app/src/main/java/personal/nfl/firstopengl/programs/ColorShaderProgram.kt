/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package personal.nfl.firstopengl.programs

import android.content.Context
import android.opengl.GLES20
import personal.nfl.firstopengl.R

class ColorShaderProgram(context: Context?) : ShaderProgram(
    context!!, R.raw.simple_vertex_shader,
    R.raw.simple_fragment_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int
    private val uColorLocation: Int

    // Attribute locations
    val positionAttributeLocation: Int
    val colorAttributeLocation: Int
    fun setUniforms(matrix: FloatArray) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f)
    }

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        // Retrieve attribute locations for the shader program.
        positionAttributeLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        colorAttributeLocation = GLES20.glGetAttribLocation(program, A_COLOR)
    }
}