package personal.nfl.firstopengl.util

import android.util.Log

class LogUtil {

    companion object{
        private const val TAG = "NFL"
        fun e(log:String){
            Log.e(TAG , log)
        }
        fun d(log:String){
            Log.d(TAG , log)
        }
    }
}