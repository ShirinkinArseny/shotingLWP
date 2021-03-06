package com.acidspacecompany.shotinglwp.OpenGLWrapping.Generators;

import android.content.Context;

import static android.opengl.GLES20.*;

public class ShaderGenerator {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }
    public static int compileVertexShader(Context context, int resourceId) {
        return compileVertexShader(TextResourceReader.readTextFileFromResource(context,resourceId));
    }
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }
    public static int compileFragmentShader(Context context, int resourceId) {
        return compileFragmentShader(TextResourceReader.readTextFileFromResource(context,resourceId));
    }

    private static int compileShader(int type, String shaderCode){
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            //todo: BicycleDebugger.w(TAG, "Could not create shader.");
        }
        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        //Выводим в лог информацию о компиляции
        //todo: BicycleDebugger.v(TAG, "Results of compiling source:\n" + shaderCode + '\n' + glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            //todo: BicycleDebugger.w(TAG, "Compilation of shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId, boolean deleteShaders) {
        final int programObjectId = glCreateProgram();

        if (programObjectId==0) {
            //todo: BicycleDebugger.w(TAG, "Could not create new program.");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        //todo: BicycleDebugger.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            //todo: BicycleDebugger.w(TAG, "Linking of program failed.");
            return 0;
        }

        if (deleteShaders) {
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
        }

        return programObjectId;
    }

    public static int createProgram(String vertexShader, String fragmentShader)
    {
        return linkProgram(compileVertexShader(vertexShader), compileFragmentShader(fragmentShader), true);
    }
    public static int createProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        return linkProgram(compileVertexShader(context, vertexShaderResourceId), compileFragmentShader(context, fragmentShaderResourceId), true);
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        //todo: BicycleDebugger.v(TAG, "Results of validating:\n" + validateStatus[0] + "\nLog" +
              //  glGetProgramInfoLog(programObjectId));
        return validateStatus[0]!=0;
    }
}
