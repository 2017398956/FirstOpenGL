/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package personal.nfl.firstopengl.objects

import personal.nfl.firstopengl.data.VertexArray
import personal.nfl.firstopengl.programs.ColorShaderProgram
import personal.nfl.firstopengl.util.Geometry

class Puck(radius: Float, height: Float, numPointsAroundPuck: Int) {
    val radius: Float
    val height: Float
    private val vertexArray: VertexArray
    private val drawList: List<ObjectBuilder.DrawCommand>
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.positionAttributeLocation,
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        for (drawCommand in drawList) {
            drawCommand.draw()
        }
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
    }

    init {
        val generatedData: ObjectBuilder.GeneratedData = ObjectBuilder.createPuck(
            Geometry.Cylinder(
                Geometry.Point(0f, 0f, 0f), radius, height
            ), numPointsAroundPuck
        )
        this.radius = radius
        this.height = height
        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }
}