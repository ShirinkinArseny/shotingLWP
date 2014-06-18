package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import android.opengl.GLES20;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Primitive {

    private int bufferId;
    //Количество вершин
    private int pointsNum;
    protected int getPointsNum() { return pointsNum; }

    public void startDraw() {
        Graphic.startDrawPrimitive(bufferId);
    }
    public void draw(float x, float y, float scaleX, float scaleY, float angle) {
    }
    public void draw(float x,float y, float scale, float angle) {
        draw(x,y,scale,scale,angle);
    }

    public Primitive(float[] vertexes) {
        bufferId = Graphic.createPrimitive(vertexes);
        pointsNum = vertexes.length / 2;
    }

}
