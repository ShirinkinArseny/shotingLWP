package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class FillPrimitive extends LinePrimitive {
    private float r,g,b,a;

    @Override
    public void draw(float x, float y, float scaleX, float scaleY, float sin, float cos) {
        Graphic.drawTriangleFan();
        Graphic.drawPrimitive(getPointsNum(),r,g,b,a,x,y,scaleX,scaleY,sin,cos);
        super.drawLooped(x,y,scaleX,scaleY,sin,cos);
    }

    @Override
    public void draw(float x, float y, float scale, float sin, float cos) {
        draw(x,y,scale,scale,sin,cos);
    }

    public FillPrimitive(float[] vertexes, float r, float g, float b, float a, float lineWidth,
                         float fillR, float fillG, float fillB, float fillA) {
        super(vertexes, r, g, b, a, lineWidth);
        this.a=fillA;
        this.r=fillR;
        this.g=fillG;
        this.b=fillB;
    }
}
