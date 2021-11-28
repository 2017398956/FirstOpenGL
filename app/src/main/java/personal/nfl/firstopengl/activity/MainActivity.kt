package personal.nfl.firstopengl.activity

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import personal.nfl.firstopengl.renderers.AirHockeyRenderer
import personal.nfl.firstopengl.renderers.FirstRenderer
import personal.nfl.firstopengl.util.PhoneInfo

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView:GLSurfaceView
    // 用于记录 glSurfaceView 是否处于有效状态
    private var rendererSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        // 当模拟器选择使用 OpenGL 后，但不能正确运行可以加上下面的配置试试
        // glSurfaceView.setEGLConfigChooser(8 , 8 , 8 , 8 , 16 , 0)
        if (PhoneInfo.supportES2(this)){
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(AirHockeyRenderer())
            rendererSet = true
        }else{
            Toast.makeText(this , "不支持 OPENGL ES2.0" , Toast.LENGTH_SHORT).show()
        }

        setContentView(glSurfaceView)

        // setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet){
            glSurfaceView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet){
            glSurfaceView.onPause()
        }
    }


}