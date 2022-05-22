package personal.nfl.firstopengl.util

import android.opengl.Matrix
import android.os.Build
import personal.nfl.firstopengl.BuildConfig
import kotlin.math.tan

class MatrixHelper {

    companion object{
        fun perspectiveM(m: FloatArray , yFovInDegrees:Float , aspect:Float , n:Float , f:Float){
            if (Build.VERSION.SDK_INT > 15){
                Matrix.perspectiveM(m , 0 , yFovInDegrees , aspect , n , f)
            }else{
                // 1. 基于 Y 轴上的视野计算焦距
                val angleInRadians = (yFovInDegrees * Math.PI / 180f).toFloat()
                val a = 1f / tan(angleInRadians / 2f)

                m[0] = a / aspect
                m[1] = 0f
                m[2] = 0f
                m[3] = 0f

                m[4] = 0f
                m[5] = a
                m[6] = 0f
                m[7] = 0f

                m[8] = 0f
                m[9] = 0f
                m[10] = -((f + n) / (f - n))
                m[11] = -1f

                m[12] = 0f
                m[13] = 0f
                m[14] = -((2f * f * n) / (f - n))
                m[15] = 0f
            }
        }
    }
}