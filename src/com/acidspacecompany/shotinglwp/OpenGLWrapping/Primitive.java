package com.acidspacecompany.shotinglwp.OpenGLWrapping;

public class Primitive {

    private int bufferId;
    //Количество вершин
    private int pointsNum;
    //Цвет
    private float r,g,b,a;
    //Ширина линии
    private float lineWidth;

    public void startDraw() {
        Graphic.startDrawPrimitive(bufferId);
    }
    public void draw(float x, float y) {
        Graphic.drawPrimitive(pointsNum, r,g,b,a,lineWidth,x,y);
    }

    public Primitive(float[] vertexes, float r,float g, float b, float a, float lineWidth) {
        bufferId = Graphic.createPrimitive(vertexes);
        pointsNum = vertexes.length / 2;
        this.r = r; this.g = g; this.b = b; this.a = a;
        this.lineWidth = lineWidth;
    }

}
