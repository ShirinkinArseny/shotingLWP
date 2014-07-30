package com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders;

import android.content.Context;
import android.opengl.GLES20;
import com.acidspacecompany.shotinglwp.R;

public class ThresholdTextureShader extends TextureShader {

    @Override
    protected int getFragmentShaderID() {
        return R.raw.threshold_fragment_shader;
    }

    private int uTh;
    public void setTh(float th) {
        GLES20.glUniform1f(uTh, th);
    }
    private int uThLVL;
    public void setThLVL(float ThLVL) {
        GLES20.glUniform1f(uThLVL, ThLVL);
    }
    private int uV2;
    public void setV2(float V2) {
        GLES20.glUniform1f(uV2, V2);
    }

    public ThresholdTextureShader(Context context) {
        super(context);
        uTh = GLES20.glGetUniformLocation(programId, "u_Th");
        uThLVL = GLES20.glGetUniformLocation(programId, "u_ThLVL");
        uV2 = GLES20.glGetUniformLocation(programId, "u_V2");
    }
}
