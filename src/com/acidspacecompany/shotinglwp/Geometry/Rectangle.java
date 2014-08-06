package com.acidspacecompany.shotinglwp.Geometry;

public class Rectangle extends ConvexPolygon{

    private float w2;
    private float h2;

    public float getW() {
        return w2;
    }

    public float getH() {
        return h2;
    }

    public Rectangle(float x, float y, float w, float h, float angle) {
        super(new Point[]{
                new Point(x+w, y-h).getRotated(angle, x, y),
                new Point(x+w, y+h).getRotated(angle, x, y),
                new Point(x-w, y+h).getRotated(angle, x, y),
                new Point(x-w, y-h).getRotated(angle, x, y),
        });
        this.w2=w;
        this.h2=h;
    }

    public Rectangle(float x, float y, float w, float h) {
        super(new Point[]{
                new Point(x+w/2, y-h/2),
                new Point(x+w/2, y+h/2),
                new Point(x-w/2, y+h/2),
                new Point(x-w/2, y-h/2),
        });
        this.w2=w;
        this.h2=h;
    }

    public Rectangle(Point p, int w, int h) {
        this(p.getX(), p.getY(), w, h);
    }
}