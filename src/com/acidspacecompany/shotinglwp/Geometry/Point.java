package com.acidspacecompany.shotinglwp.Geometry;

public class Point {

    float x;
    float y;

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

    public Point negate() {
        return new Point(-x, -y);
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

    private static final double rightAngle=Math.PI/2;

    public double getSquaredDistanceToSegment(Segment s) {
        double angle1=Utils.getAngle(this, s.getStart(), s.getEnd());
        if (angle1>rightAngle) return s.getStart().getSquaredDistanceToPoint(this);
        double angle2=Utils.getAngle(this, s.getEnd(), s.getStart());
        if (angle2>rightAngle) return s.getEnd().getSquaredDistanceToPoint(this);
        return Utils.getSquaredLengthToLine(this, s.getStart(), s.getEnd());
    }

    public void rotate(float angle, Point center) {

        float ax = x - center.x;
        float ay = y - center.y;

        double newX = ax * Math.cos(angle) - ay * Math.sin(angle);
        double newY = ax * Math.sin(angle) + ay * Math.cos(angle);
        x = (float) newX + center.x;
        y = (float) newY + center.y;
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
        return Math.ceil(x) + " " +  Math.ceil(y);
    }
}