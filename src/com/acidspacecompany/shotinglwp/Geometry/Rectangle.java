package com.acidspacecompany.shotinglwp.Geometry;

public class Rectangle extends ConvexPolygon{

    public Rectangle(float x, float y, int w, int h) {
        super(new Point[]{
                new Point(x-w/2, y-h/2),
                new Point(x+w/2, y-h/2),
                new Point(x+w/2, y+h/2),
                new Point(x-w/2, y+h/2),
        });
    }

    public Rectangle(Point p, int w, int h) {
        this(p.getX(), p.getY(), w, h);
    }
}