package com.acidspacecompany.shotinglwp.Geometry;

public class Point {

    float x;
    float y;

    public static float getScalarMultiply(Point a, Point b) {
        return a.x*b.x + a.y*b.y;
    }
    public static float getScalarMultiply(Segment a, Segment b) {
        return a.getDx()*b.getDx() + a.getDy()*b.getDy();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Point add(Point v2) {
        return add(x + v2.x, y + v2.y);
    }

    public Point add(float dx, float dy) {
        return new Point(x + dx, y + dy);
    }

    public double length() {
        return Math.sqrt(x*x+y*y);
    }

    public Point setLength(float length) {
        double l=length();
        if (Math.abs(l-length)<=Utils.epsilon) return this;
        return this.multiply(length/l);
    }

    public Point getNormal() {
        float a1 = y;
        float b1 = - x;
        return new Point(a1, b1);
    }

    public float getSquaredDistanceToPoint(Point p2) {
        float dx = x - p2.x;
        float dy = y - p2.y;
        return dx * dx + dy * dy;
    }

    public double getDistanceToPoint(Point p) {
        return Math.sqrt(getSquaredDistanceToPoint(p));
    }

    public void rotate(float angle, float x, float y) {

        float ax = this.x - x;
        float ay = this.y - y;

        this.x=(float) (ax * Math.cos(angle) - ay * Math.sin(angle)) + x;
        this.y=(float) (ax * Math.sin(angle) + ay * Math.cos(angle)) + y;
    }

    public void rotate(float angle, Point center) {
        rotate(angle, center.x, center.y);
    }

    public Point getRotated(float angle, float xp, float yp) {

        float ax = x - xp;
        float ay = y - yp;

        double newX = ax * Math.cos(angle) - ay * Math.sin(angle);
        double newY = ax * Math.sin(angle) + ay * Math.cos(angle);
        return new Point(newX + xp, newY + yp);
    }

    public Point multiply(double scalar) {
        return new Point(x * scalar, y * scalar);
    }

    public void move(float dx, float dy) {
        x+=dx;
        y+=dy;
    }

    public void move(Point p) {
        move(p.x, p.y);
    }

    public void setPosition(float dx, float dy) {
        x=dx;
        y=dy;
    }

    public Point(double x, double y) {
        this((float)x, (float)y);
    }

    public Point(float x, float y) {
        this.x=x;
        this.y=y;
    }

    public Point(Point p) {
        this(p.x, p.y);
    }


    public String toString() {
        return x + " " +  y;
    }
}