package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class LinePrimitive extends Primitive {
    private float r,g,b,a;
    private float lineWidth;

    @Override
    public void draw(float x, float y, float scaleX, float scaleY, float angle) {
        Graphic.drawLinePrimitive(getPointsNum(), r, g, b, a,lineWidth, x, y, scaleX, scaleY, angle);
    }

    public LinePrimitive(float[] vertexes, float r, float g, float b, float a, float lineWidth) {
        super(vertexes);
        this.r = r; this.g = g; this.b = b; this.a = a;
        this.lineWidth = lineWidth;
    }
}
