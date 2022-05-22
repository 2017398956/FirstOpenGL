/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package personal.nfl.firstopengl.renderers

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import personal.nfl.firstopengl.R
import personal.nfl.firstopengl.objects.Mallet2
import personal.nfl.firstopengl.objects.Puck
import personal.nfl.firstopengl.objects.Table
import personal.nfl.firstopengl.programs.ColorShaderProgram
import personal.nfl.firstopengl.programs.TextureShaderProgram
import personal.nfl.firstopengl.util.MatrixHelper
import personal.nfl.firstopengl.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyWithImageRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private lateinit var table: Table
    private lateinit var mallet: Mallet2
    private lateinit var puck: Puck
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture = 0

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private val useEye = true

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        table = Table()
        mallet = Mallet2(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix,
            45f,
            width.toFloat() / height.toFloat(),
            1f,
            10f
        )
        if (useEye) {
            Matrix.setLookAtM(
                viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f,
                0f, 0f, 1f, 0f
            )
        } else {
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.7f)
            Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)
            val temp = FloatArray(16)
            Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
            System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
        }
    }

    override fun onDrawFrame(glUnused: GL10) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        if (useEye){
            // Multiply the view and projection matrices together.
            Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
            // Draw the table.
            positionTableInScene()
            textureProgram.useProgram()
            textureProgram.setUniforms(modelViewProjectionMatrix , texture)
            table.bindData(textureProgram)
            table.draw()

            positionObjectInScene(0f , mallet.height / 2f , -0.4f)
            colorProgram.useProgram()
            colorProgram.setUniforms(modelViewProjectionMatrix , 1f , 0f , 0f)
            mallet.bindData(colorProgram)
            mallet.draw()

            positionObjectInScene(0f , mallet.height / 2f , 0.4f)
            colorProgram.setUniforms(modelViewProjectionMatrix , 0f , 0f , 1f)
            mallet.draw()

            positionObjectInScene(0f , puck.height / 2f , 0f)
            colorProgram.setUniforms(modelViewProjectionMatrix , 0f , 0f , 1f)
            puck.bindData(colorProgram)
            puck.draw()
        }else{
            // Draw the table.
            textureProgram.useProgram()
            textureProgram.setUniforms(projectionMatrix, texture)
            table.bindData(textureProgram)
            table.draw()

            // Draw the mallets.
            colorProgram.useProgram()
            colorProgram.setUniforms(projectionMatrix)
            mallet.bindData(colorProgram)
            mallet.draw()
        }
    }

    private fun positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x :Float , y:Float , z:Float){
        Matrix.setIdentityM(modelMatrix , 0)
        Matrix.translateM(modelMatrix , 0 , x , y , z)
        Matrix.multiplyMM(modelViewProjectionMatrix , 0 , viewProjectionMatrix , 0 , modelMatrix , 0)
    }
}