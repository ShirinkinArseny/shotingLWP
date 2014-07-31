package com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders;

import android.content.Context;
import android.opengl.GLES20;
import com.acidspacecompany.shotinglwp.R;

public class AnimationTextureShader extends TextureShader {

    @Override
    protected int getFragmentShaderID() {
        return R.raw.animation_fragment_shader;
    }

    private int uWidth;
    public void setWidth(int width) {
        GLES20.glUniform1i(uWidth, width);
    }

    private int uPosition;
    public void setPosition(int position) {
        GLES20.glUniform1i(uPosition, position);
    }

    public AnimationTextureShader(Context context) {
        super(context);
        uWidth = GLES20.glGetUniformLocation(getProgramId(), "u_Width");
        uPosition = GLES20.glGetUniformLocation(getProgramId(), "u_Position");
    }
}
