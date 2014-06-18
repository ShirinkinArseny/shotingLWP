package com.acidspacecompany.shotinglwp.Geometry;

public class Rectangle extends Point{
    private int w;
    private int h;
    private int w2;
    private int h2;
    private float x0, y0, x1, y1;

    public float getHalfHeight() {
        return h2;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public float getHalfWidth() {
        return w2;
    }

    public Rectangle(float x, float y, int w, int h) {
        super(x, y);
        this.w=w;
        this.h=h;
        w2=w/2;
        h2=h/2;
        super.setPosition(x, y);
        x0=x-w2;
        y0=y-h2;
        x1=x+w2;
        y1=y+h2;
    }


    public void setPosition(float dx, float dy) {
        super.setPosition(dx, dy);
        x0=dx-w2;
        y0=dy-h2;
        x1=dx+w2;
        y1=dy+h2;
    }

    public void move(float dx, float dy) {
        super.move(dx, dy);
        x0+=dx;
        y0+=dy;
        x1+=dx;
        y1+=dy;
    }

    public float getX0() {
        return x0;
    }

    public float getY0() {
        return y0;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public Rectangle(Point p, int w, int h) {
        this(p.getX(), p.getY(), w, h);
    }

    public boolean getIntersect(Rectangle m) {
        return (Math.abs(getX()-m.getX()))<=(m.w2+w2) &&
               (Math.abs(getY()-m.getY()))<=(m.h2+h2);
    }

    public void setSize(int w, int h) {
        this.w=w;
        this.h=h;
        w2=w/2;
        h2=h/2;
        setPosition(getX(), getY());
    }
}