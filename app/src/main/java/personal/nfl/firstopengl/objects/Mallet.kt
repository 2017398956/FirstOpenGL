/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package personal.nfl.firstopengl.objects

import android.opengl.GLES20
import personal.nfl.firstopengl.data.VertexArray
import personal.nfl.firstopengl.programs.ColorShaderProgram
import personal.nfl.firstopengl.util.Constants.Companion.BYTES_PER_FLOAT

class Mallet {
    private val vertexArray: VertexArray
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.positionAttributeLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            colorProgram.colorAttributeLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private val STRIDE: Int = ((POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
                * BYTES_PER_FLOAT)
        private val VERTEX_DATA = floatArrayOf( // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
        )
    }

    init {
        vertexArray = VertexArray(VERTEX_DATA)
    }
}