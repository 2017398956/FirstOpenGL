package personal.nfl.firstopengl.renderers

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FirstRenderer : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.i("NFL", "onSurfaceCreated")
        glClearColor(1f, 0f, 0f, 0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.i("NFL", "onSurfaceChanged")
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        Log.i("NFL", "onDrawFrame")
        glClear(GL_COLOR_BUFFER_BIT)
    }

}