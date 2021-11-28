package personal.nfl.firstopengl.util

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader

class TextResourceReader {
    companion object {

        private var context:Context? = null

        fun init(context: Context){
            if (null == context){
                throw IllegalArgumentException("TextResourceReader 初始化时，context 不能为 null")
            }
            TextResourceReader.context = context
        }

        fun readTextFileFromResource(@RawRes resId: Int): String {
            if (null == context){
                throw IllegalArgumentException("TextResourceReader 未初始化。")
            }
           return readTextFileFromResource(context!! , resId)
        }
        private fun readTextFileFromResource(context: Context, @RawRes resId: Int): String {
            if (null == context){
                throw IllegalArgumentException("context 不能为 null")
            }
            val body = StringBuilder()
            val inputStream = context.resources.openRawResource(resId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String? = null
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append("\n")
            }
            return body.toString()
        }
    }
}