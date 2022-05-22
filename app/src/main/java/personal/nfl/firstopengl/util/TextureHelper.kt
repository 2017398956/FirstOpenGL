package personal.nfl.firstopengl.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.annotation.DrawableRes

class TextureHelper {
    companion object {
        fun loadTexture(context: Context, @DrawableRes resId: Int): Int {
            val textureObjectIds = IntArray(1)
            GLES20.glGenTextures(1, textureObjectIds, 0)
            if (textureObjectIds[0] == 0) {
                LogUtil.e("Could not generate a new OpenGL texture Object.")
                return 0
            }
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(context.resources, resId, options)
            if (bitmap == null) {
                LogUtil.e("Resource Id $resId could be decoded.")
                GLES20.glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
            // 完成纹理加载后，对这个纹理进行解绑，这样其它的纹理操作就不会影响这个纹理了
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            return textureObjectIds[0]
        }
    }
}