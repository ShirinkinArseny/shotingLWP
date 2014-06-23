package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.ConvexPolygon;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives.FillPrimitive;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives.LinePrimitive;

import java.util.ArrayList;

public class Building extends ConvexPolygon{

    private float width;
    private float height;
    private float angle;
    private float sin;
    private float cos;
    private static LinePrimitive roof;
    private static FillPrimitive home;

    public static void startDrawBase() {
        home.startDraw();
    }

    public static void startDrawRoof() {
        roof.startDraw();
    }

    public static void init(float lineWidth) {
        float[] vertexes=new float[8];
        vertexes[0]= -1;
        vertexes[1]= -1;
        vertexes[2]= 1;
        vertexes[3]= -1;
        vertexes[4]= 1;
        vertexes[5]= 1;
        vertexes[6]= -1;
        vertexes[7]= 1;
        for (int i=0; i<8; i++)
            vertexes[i]/=2;
        home=new FillPrimitive(vertexes, 0, 0, 0, 1, lineWidth, 1, 1, 1, 1);
        vertexes=new float[20];
        vertexes[0]= -1;
        vertexes[1]= -1;
        vertexes[2]= -0.7f;
        vertexes[3]= 0;

        vertexes[4]= -1;
        vertexes[5]= 1;
        vertexes[6]= -0.7f;
        vertexes[7]= 0;

        vertexes[8]= 1;
        vertexes[9]= -1;
        vertexes[10]= 0.7f;
        vertexes[11]= 0;

        vertexes[12]= 1;
        vertexes[13]= 1;
        vertexes[14]= 0.7f;
        vertexes[15]= 0;

        vertexes[16]= -0.7f;
        vertexes[17]= 0;
        vertexes[18]= 0.7f;
        vertexes[19]= 0;

        for (int i=0; i<20; i++)
            vertexes[i]/=2;

        roof=new LinePrimitive(vertexes, 0, 0, 0, 1, lineWidth);
    }

    public void drawRoof() {
        roof.draw(getCentre().getX(), getCentre().getY(), width, height, sin, cos);
    }

    public void drawBase() {
        home.draw(getCentre().getX(), getCentre().getY(), width, height, sin, cos);
    }

    public boolean getIsIntersect(Segment s) {
        for (Segment s2: getSegments())
        if (s.getIsIntersects(s2))
            return true;
        return false;
    }

    public Building(float x, float y, float w, float h, float angle) {
        super(new Point[]{
                new Point(x+w/2, y-h/2).getRotated(angle, new Point(x, y)),
                new Point(x+w/2, y+h/2).getRotated(angle, new Point(x, y)),
                new Point(x-w/2, y+h/2).getRotated(angle, new Point(x, y)),
                new Point(x-w/2, y-h/2).getRotated(angle, new Point(x, y))
        });
        width=w;
        height=h;
        this.angle=angle;

        sin = (float) Math.sin(angle);
        cos = (float) Math.cos(angle);
    }
}