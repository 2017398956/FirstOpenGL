package personal.nfl.firstopengl.util

import android.opengl.GLES20

class ShaderHelper {

    companion object{
        fun compileVertexShader(shaderCode:String):Int{
            return compileShader(GLES20.GL_VERTEX_SHADER , shaderCode)
        }

        fun compileFragmentShader(shaderCode: String):Int{
            return compileShader(GLES20.GL_FRAGMENT_SHADER , shaderCode)
        }

        private fun compileShader(type:Int, shaderCode: String):Int{
            val shaderObjectId = GLES20.glCreateShader(type)
            if (shaderObjectId == 0){
                LogUtil.e("Could not create new shader.")
                return 0
            }
            GLES20.glShaderSource(shaderObjectId , shaderCode)
            GLES20.glCompileShader(shaderObjectId)
            LogUtil.d("Results of compiling source:\n$shaderCode\n:${GLES20.glGetShaderInfoLog(shaderObjectId)}")
            val vertexShaderStatus = IntArray(1)
            GLES20.glGetShaderiv(
                shaderObjectId,
                GLES20.GL_COMPILE_STATUS,
                vertexShaderStatus,
                0
            )
            if (vertexShaderStatus[0] == 0){
                GLES20.glDeleteShader(shaderObjectId)
                LogUtil.e("Compilation of shader failed")
                return 0
            }
            return shaderObjectId
        }

        fun linkProgram(vertexShaderId:Int , fragmentShaderId:Int):Int{
            val programObjectId = GLES20.glCreateProgram()
            if (programObjectId == 0){
                LogUtil.e("Could not create new program.")
                return 0
            }
            GLES20.glAttachShader(programObjectId , vertexShaderId)
            GLES20.glAttachShader(programObjectId , fragmentShaderId)
            GLES20.glLinkProgram(programObjectId)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programObjectId , GLES20.GL_LINK_STATUS , linkStatus , 0)
            LogUtil.d("Results of linking program:\n${GLES20.glGetProgramInfoLog(programObjectId)}")
            if (linkStatus[0] == 0){
                GLES20.glDeleteProgram(programObjectId)
                LogUtil.e("Linking of program failed.")
                return 0
            }
            return programObjectId
        }

        fun validateProgram(programObjectId :Int):Boolean{
            GLES20.glValidateProgram(programObjectId)
            val validateStatus = IntArray(1)
            GLES20.glGetProgramiv(programObjectId , GLES20.GL_VALIDATE_STATUS , validateStatus , 0)
            LogUtil.d("Results of validating program:${validateStatus[0]}\nlog:${GLES20.glGetProgramInfoLog(programObjectId)}")
            return validateStatus[0] != 0
        }
    }
}