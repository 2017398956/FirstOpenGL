package personal.nfl.firstopengl

import android.app.Application
import android.content.Context
import personal.nfl.firstopengl.util.TextResourceReader

class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        TextResourceReader.init(base!!)
    }
}