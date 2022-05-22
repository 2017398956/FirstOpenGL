package personal.nfl.firstopengl.objects

import android.opengl.GLES20.*
import personal.nfl.firstopengl.util.Geometry
import kotlin.math.cos
import kotlin.math.sin


class ObjectBuilder(val sizeInVertices: Int) {
    private val FLOATS_PER_VERTEX = 3

    interface DrawCommand {
        fun draw()
    }

    class GeneratedData(val vertexData: FloatArray, val drawList: List<DrawCommand>)

    private val vertexData: FloatArray

    // 记录数组中下一个顶点的位置
    private var offset = 0
    private val drawList: ArrayList<DrawCommand> = ArrayList()

    init {
        vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX)
    }

    private fun appendCircle(circle: Geometry.Circle, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        // Center point of fan
        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z

        // Fan around center point. <= is used because we want to generate
        // the point at the starting angle twice to complete the fan.
        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat()
                    * (Math.PI.toFloat() * 2f))
            vertexData[offset++] = (circle.center.x
                    + circle.radius * cos(angleInRadians))
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = (circle.center.z
                    + circle.radius * sin(angleInRadians))
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    private fun appendOpenCylinder(cylinder: Geometry.Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart: Float = cylinder.center.y - cylinder.height / 2f
        val yEnd: Float = cylinder.center.y + cylinder.height / 2f

        // Generate strip around center point. <= is used because we want to
        // generate the points at the starting angle twice, to complete the
        // strip.
        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat()
                    * (Math.PI.toFloat() * 2f))
            val xPosition: Float = (cylinder.center.x
                    + cylinder.radius * cos(angleInRadians))
            val zPosition: Float = (cylinder.center.z
                    + cylinder.radius * sin(angleInRadians))
            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition
            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    private fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }

    companion object {
        // 计算圆柱顶部顶点数量
        private fun sizeOfCircleInVertices(numPoints: Int): Int {
            return 1 + (numPoints + 1)
        }

        // 计算圆柱体侧面顶点的数量
        private fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
            return (numPoints + 1) * 2
        }

        fun createPuck(puck: Geometry.Cylinder, numPoints: Int): GeneratedData {
            val size: Int = (sizeOfCircleInVertices(numPoints)
                    + sizeOfOpenCylinderInVertices(numPoints))
            val builder = ObjectBuilder(size)
            val puckTop = Geometry.Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius
            )
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)
            return builder.build()
        }

        fun createMallet(
            center: Geometry.Point, radius: Float, height: Float, numPoints: Int
        ): GeneratedData {
            val size = (sizeOfCircleInVertices(numPoints) * 2
                    + sizeOfOpenCylinderInVertices(numPoints) * 2)
            val builder = ObjectBuilder(size)

            // First, generate the mallet base.
            val baseHeight = height * 0.25f
            val baseCircle = Geometry.Circle(
                center.translateY(-baseHeight),
                radius
            )
            val baseCylinder = Geometry.Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius, baseHeight
            )
            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)

            // Now generate the mallet handle.
            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f
            val handleCircle = Geometry.Circle(
                center.translateY(height * 0.5f),
                handleRadius
            )
            val handleCylinder = Geometry.Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius, handleHeight
            )
            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)
            return builder.build()
        }
    }
}