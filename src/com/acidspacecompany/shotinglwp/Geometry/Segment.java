package com.acidspacecompany.shotinglwp.Geometry;

public class Segment {

    private Point start;
    private Point end;
    private float length;

    public void setStart(Point p) {
        start = p;
        length= (float) start.getDistanceToPoint(end);
    }

    public void setEnd(Point p) {
        end = p;
        length= (float) start.getDistanceToPoint(end);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public float getLength() {
        return length;
    }

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getIntersection(Segment s) {
        return Utils.getIntersection(start, end, s.start, s.end);
    }

    public Segment(float x1, float y1, float x2, float y2) {
        start = new Point(x1, y1);
        end = new Point(x2, y2);
        length= (float) start.getDistanceToPoint(end);
    }

}
