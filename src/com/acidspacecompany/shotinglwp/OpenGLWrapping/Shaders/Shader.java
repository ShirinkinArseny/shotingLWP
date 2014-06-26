package com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders;

import java.nio.FloatBuffer;

public interface Shader {

    public void setMatrix(FloatBuffer matrix);
    public void setMatrix(float[] matrix, int offset);
    public int get_aPosition();
    public void validate();
    public void use();
    public void delete();



}
