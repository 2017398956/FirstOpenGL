package personal.nfl.firstopengl.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build

class PhoneInfo {

    companion object {
        fun supportES2(context: Context): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return activityManager.deviceConfigurationInfo.reqGlEsVersion >= 0x20000
                    // 兼容模拟器，如果是模拟器就假定支持 es2
                    || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                    && (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK build for x86")
                    )
                    )
        }
    }
}