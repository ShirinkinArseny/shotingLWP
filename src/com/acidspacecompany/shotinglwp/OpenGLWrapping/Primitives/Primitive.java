package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Primitive {

    private int bufferId;
    //Количество вершин
    private int pointsNum;
    protected int getPointsNum() { return pointsNum; }

    public void startDraw() {
        Graphic.startDrawPrimitive(bufferId);
    }
    public void draw(float x, float y, float scaleX, float scaleY, float sin, float cos) {
    }
    public void draw(float x,float y, float scale, float sin, float cos) {
        draw(x,y,scale,scale,sin,cos);
    }

    public Primitive(float[] vertexes) {
        bufferId = Graphic.createPrimitive(vertexes);
        pointsNum = vertexes.length / 2;
    }

}
