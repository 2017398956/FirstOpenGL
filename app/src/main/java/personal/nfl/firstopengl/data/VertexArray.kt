package personal.nfl.firstopengl.data

import android.opengl.GLES20
import personal.nfl.firstopengl.util.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexArray {
    private var floatBuffer: FloatBuffer

    constructor(vertexData: FloatArray) {
        floatBuffer = ByteBuffer.allocateDirect(vertexData.size * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData)
    }

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GLES20.GL_FLOAT,
            false,
            stride,
            floatBuffer
        )
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}