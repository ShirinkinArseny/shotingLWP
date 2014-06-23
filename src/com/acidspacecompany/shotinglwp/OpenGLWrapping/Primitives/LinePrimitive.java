package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class LinePrimitive extends Primitive {
    private float r,g,b,a;
    private float lineWidth;

    protected void drawLooped(float x, float y, float scaleX, float scaleY, float sin, float cos)
    {
        Graphic.drawLineLoop();
        Graphic.setLineWidth(lineWidth);
        Graphic.drawPrimitive(getPointsNum(), r, g, b, a, x, y, scaleX, scaleY, sin,cos);
    }

    @Override
    public void draw(float x, float y, float scaleX, float scaleY, float sin, float cos) {
        Graphic.drawLines();
        Graphic.setLineWidth(lineWidth);
        Graphic.drawPrimitive(getPointsNum(), r, g, b, a, x, y, scaleX, scaleY, sin,cos);
    }

    @Override
    public void draw(float x, float y, float scale, float sin, float cos) {
        draw(x, y, scale, scale, sin, cos);
    }

    public LinePrimitive(float[] vertexes, float r, float g, float b, float a, float lineWidth) {
        super(vertexes);
        this.r = r; this.g = g; this.b = b; this.a = a;
        this.lineWidth = lineWidth;
    }

    public void drawWithAlpha(float x, float y, float scale, float sin, float cos, float alpha) {
        Graphic.drawLines();
        Graphic.setLineWidth(lineWidth);
        Graphic.drawPrimitive(getPointsNum(), r, g, b, alpha, x, y, scale, scale, sin, cos);
    }
}
