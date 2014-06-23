package com.acidspacecompany.shotinglwp.Geometry;

public class Utils {

    public static final float epsilon = 0.0001f;

    //check lines p1p2 and line p3p4
    public static boolean getAreSegmentsIntersect(Point p1, Point p2, Point p3, Point p4) {
        float a1 = p2.y - p1.y;
        float a2 = p4.y - p3.y;
        float b1 = p1.x - p2.x;
        float b2 = p3.x - p4.x;
        float det = a1 * b2 - a2 * b1;
        if (Math.abs(det) <= Utils.epsilon) return false;
        float c1 = p1.y * b1 + p1.x * a1;
        float c2 = p3.y * b2 + p3.x * a2;
        float det1 = c1 * b2 - c2 * b1;
        float det2 = a1 * c2 - a2 * c1;
        float x = det1 / det;
        float y = det2 / det;
        boolean inX12 = Math.min(p1.x, p2.x) <= x && x <= Math.max(p1.x, p2.x);
        boolean inX34 = Math.min(p3.x, p4.x) <= x && x <= Math.max(p3.x, p4.x);
        boolean inY12 = Math.min(p1.y, p2.y) <= y && y <= Math.max(p1.y, p2.y);
        boolean inY34 = Math.min(p3.y, p4.y) <= y && y <= Math.max(p3.y, p4.y);
        if (inX12 && inX34 && inY12 && inY34) return false;
        return true;
    }

    //check lines p1p2 and line p3p4
    public static Point getSegmentsIntersection(Point p1, Point p2, Point p3, Point p4) {
        float a1 = p2.y - p1.y;
        float a2 = p4.y - p3.y;
        float b1 = p1.x - p2.x;
        float b2 = p3.x - p4.x;
        float det = a1 * b2 - a2 * b1;
        if (Math.abs(det) <= Utils.epsilon) return null;
        float c1 = p1.y * b1 + p1.x * a1;
        float c2 = p3.y * b2 + p3.x * a2;
        float det1 = c1 * b2 - c2 * b1;
        float det2 = a1 * c2 - a2 * c1;
        float x = det1 / det;
        float y = det2 / det;
        boolean inX12 = Math.min(p1.x, p2.x) <= x && x <= Math.max(p1.x, p2.x);
        boolean inX34 = Math.min(p3.x, p4.x) <= x && x <= Math.max(p3.x, p4.x);
        boolean inY12 = Math.min(p1.y, p2.y) <= y && y <= Math.max(p1.y, p2.y);
        boolean inY34 = Math.min(p3.y, p4.y) <= y && y <= Math.max(p3.y, p4.y);
        if (inX12 && inX34 && inY12 && inY34) return new Point(x, y);
        return null;
    }

    public static double getTriangleSquare(Point p1, Point p2, Point p3) {

        double l1=p1.getDistanceToPoint(p2);
        double l2=p1.getDistanceToPoint(p3);
        double l3=p2.getDistanceToPoint(p3);

        double halfPerimeter=l1+l2+l3;
        halfPerimeter/=2;

        return Math.sqrt(halfPerimeter*(halfPerimeter-l1)*(halfPerimeter-l2)*(halfPerimeter-l3));
    }

    public static double getSquaredLengthToLine
            (Point from, Point ptOnLine1, Point ptOnLine2) {
        float a = ptOnLine1.y - ptOnLine2.y;
        float b = ptOnLine2.x - ptOnLine1.x;
        float c = ptOnLine1.x * ptOnLine2.y - ptOnLine2.x * ptOnLine1.y;
        float midRes=a * from.x + b * from.y + c;
        double result = midRes*midRes / (a * a + b * b);
        if (Math.abs(result) <= epsilon)
            return 0;
        else return result;
    }

    //p1p2p3-angle
    public static Point getBisection(Point p1, Point p2, Point p3) {
        Point pl1=new Point(p2.x-p1.x, p2.y-p1.y);
        Point pl2=new Point(p2.x-p3.x, p2.y-p3.y);
        pl1.setLength(1);
        pl2.setLength(1);
        if (pl1.x+pl2.x+pl1.y+pl2.y <=epsilon*4) {
            return pl1.getNormal().setLength(1);
        }
        return new Point((pl1.x+pl2.x)/2, (pl1.y+pl2.y)/2).setLength(1);
    }

    public static Point getNormal(Point p1, Point p2) {
        float a1 = p2.y - p1.y;
        float b1 = p1.x - p2.x;
        return new Point(a1, b1);
    }

    //p2p1 and p2p3 - rays
    public static double getCos(Point p1, Point p2, Point p3) {
        float x1 = p1.x - p2.x;
        float x2 = p3.x - p2.x;
        float y1 = p1.y - p2.y;
        float y2 = p3.y - p2.y;
        return (x1 * x2 + y1 * y2) / Math.sqrt((x1 * x1 + y1 * y1) * (x2 * x2 + y2 * y2));
    }

}
