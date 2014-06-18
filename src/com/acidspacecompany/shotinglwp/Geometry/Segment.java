package com.acidspacecompany.shotinglwp.Geometry;

public class Segment {

    private Point start;
    private Point end;
    private float length;
    protected float cos;
    protected float sin;
    private float angle;
    private float dx, dy, sXeY_eXsY;//for

    public double getSquaredLengthToLine(Point from) {
        float midRes=-dy * from.x + dx * from.y + sXeY_eXsY;
        double result = midRes*midRes / length;
        if (Math.abs(result) <= Utils.epsilon)
            return 0;
        else return result;
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


    //p2p1 and p2p3 - rays, dx, dy - diffs between p3 and p2
    public boolean getAngleInStartIsAcute(Point p1) {
        return (p1.x - start.x) * dx + (p1.y - start.y) * dy>0;
    }

    //p2p1 and p2p3 - rays, dx, dy - diffs between p3 and p2
    public boolean getAngleInEndIsAcute(Point p1) {
        return (p1.x - end.x) * dx + (p1.y - end.y) * dy<0;
    }
}
