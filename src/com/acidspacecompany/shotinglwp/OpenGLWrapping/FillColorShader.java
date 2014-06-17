package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.opengl.GLES20;
import com.acidspacecompany.shotinglwp.R;

import java.nio.FloatBuffer;

public class FillColorShader {

    private int programId;
    public void use() {
        GLES20.glUseProgram(programId);
    }

    public void delete() {
        GLES20.glDeleteProgram(programId);
    }

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix);
    }

    public void setMatrix(float[] matrix, int offset) {
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, offset);
    }

    private int aPosition;
    public int get_aPosition() { return aPosition; }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int uColor;
    public void setColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(uColor, r,g,b,a);
    }

    public FillColorShader(Context context) {
        programId = ShaderGenerator.createProgram(context, R.raw.fillcolor_vertex_shader, R.raw.fillcolor_fragment_shader);
        uMatrix = GLES20.glGetUniformLocation(programId, "u_Matrix");
        aPosition = GLES20.glGetAttribLocation(programId, "a_Position");
        uColor = GLES20.glGetUniformLocation(programId, "u_Color");
    }




}
