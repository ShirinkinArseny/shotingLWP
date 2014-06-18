package com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class FillPrimitive extends LinePrimitive {
    private float r,g,b,a;

    @Override
    public void draw(float x, float y, float scaleX, float scaleY, float angle) {
        Graphic.drawFillPrimitive(getPointsNum(),r,g,b,a,x,y,scaleX,scaleY,angle);
        super.draw(x,y,scaleX,scaleY,angle);
    }

    public FillPrimitive(float[] vertexes, float r, float g, float b, float a, float lineWidth,
                         float fillR, float fillG, float fillB, float fillA) {
        super(vertexes, r, g, b, a, lineWidth);
    }
}
