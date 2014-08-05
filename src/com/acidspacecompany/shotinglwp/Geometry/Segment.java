package com.acidspacecompany.shotinglwp.Geometry;

public class Segment {

    private Point start;
    private Point end;
    private float length;
    protected float cos;
    protected float sin;
    private float angle;
    private float dx, dy, sXeY_eXsY, sxsydxdy;//for
    private float A, B, C;//for
    private float minx;
    private float miny;
    private float maxx;
    private float maxy;

    public Point getNormal() {
        float a1 = end.y - start.y;
        float b1 = start.x - end.x;
        return new Point(a1, b1);
    }

    public float getSide(Point point) {
        return point.getX()* dy
                -  point.getY()* dx + sxsydxdy;
    }

    public double getLengthToLine(Point from) {
        float midRes=-dy * from.x + dx * from.y + sXeY_eXsY;
        return Math.abs(midRes / length);
    }

    public void move(float dx, float dy) {
        start.move(dx, dy);
        end.move(dx, dy);
        sXeY_eXsY=start.getX() * end.getY() - end.getX() * start.getY();
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

    public Point getPointOnSegment(float prop) {
        return new Point(start.getX()+cos*prop, start.getY()+sin*prop);
    }

    public Point getIntersection(Segment s) {
        return Utils.getSegmentsIntersection(start, end, s.start, s.end);
    }

    private boolean areDifferent(float x, float y) {
        return x<0 && y>=0 || x>0 && y<=0;
    }

    public boolean getIntersects(Segment s) {
        return areDifferent(getSide(s.getStart()),  getSide(s.getEnd())) &&  areDifferent(s.getSide(start),  s.getSide(end));
    }

    public Segment(float x1, float y1, float x2, float y2) {
        start = new Point(x1, y1);
        end = new Point(x2, y2);
        length= (float) start.getDistanceToPoint(end);
        sin=(y2-y1)/length;
        cos=(x2-x1)/length;
        angle= (float) Math.atan2(y2-y1, x2-x1);
        dx=x2-x1;
        dy=y2-y1;
        sXeY_eXsY=x1 * y2 - x2 * y1;
        sxsydxdy=y1*dx-x1*dy;
        minx=Math.min(x1, x2);
        miny=Math.min(y1, y2);
        maxx=Math.max(x1, x2);
        maxy=Math.max(y1, y2);
    }

    public Segment(Point p1, Point p2) {
        this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public float getAngle() {
        return angle;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public boolean getIsBetween(Point p) {
        return getAngleInStartIsAcute(p) && getAngleInEndIsAcute(p);
    }

    //p2p1 and p2p3 - rays, dx, dy - diffs between p3 and p2
    public boolean getAngleInStartIsAcute(Point p1) {
        return (p1.x - start.x) * dx + (p1.y - start.y) * dy>0;
    }

    //p2p1 and p2p3 - rays, dx, dy - diffs between p3 and p2
    public boolean getAngleInEndIsAcute(Point p1) {
        return (p1.x - end.x) * dx + (p1.y - end.y) * dy<0;
    }
}
