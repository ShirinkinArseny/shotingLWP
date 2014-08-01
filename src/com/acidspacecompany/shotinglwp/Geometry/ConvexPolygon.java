package com.acidspacecompany.shotinglwp.Geometry;

import java.util.ArrayList;
import java.util.Collections;

public class ConvexPolygon extends Point {

    private ArrayList<Segment> segments = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();

    //Fast method
    public boolean intersectBySegmentsIntersection(ConvexPolygon c) {
        for (Segment s : segments)
            for (Segment s2 : c.segments)
                if (s.getIntersects(s2))
                    return true;
        return c.containsBySegmentSide(points.get(0)) || containsBySegmentSide(c.points.get(0));
    }

    //Slow method - a little bit slower than faster
    @Deprecated
    public boolean intersectByPointContainment(ConvexPolygon c) {
        for (Point p : points)
            if (c.containsBySegmentSide(p)) return true;
        for (Point p : c.points)
            if (containsBySegmentSide(p)) return true;
        return false;
    }

    //Fast method
    public boolean containsBySegmentSide(Point point) {
        float side = 0;
        float newSide;
        boolean isFirst = true;
        for (Segment s : segments) {
            if (isFirst) {
                side = segments.get(0).getSide(point);
                isFirst = false;
            } else {
                newSide = s.getSide(point);
                if (newSide >= 0 && side <= 0 || newSide <= 0 && side >= 0) return false;
                side = newSide;
            }
        }
        return true;
    }

    //Slow method - about 100 times slower than fast method
    @Deprecated
    private static final float Pi2 = (float) (Math.PI * 2);

    public boolean containsByAngleSummary(Point point) {
        float angle = 0;
        for (Segment s : segments) {
            angle += Math.acos(Utils.getCos(s.getStart(), point, s.getEnd()));
        }
        return Math.abs(angle - Pi2) < Utils.epsilon;
    }

    //fast
    public boolean intersectsBySegmentIntersection(Segment s) {
        for (Segment s2 : getSegments()) {
            if (s.getIntersects(s2)) {
                return true;
            }
        }
        return false;
    }

    //slow
    @Deprecated
    public boolean intersectsByPointContainment(Segment s) {
        return containsBySegmentSide(s.getEnd()) || containsBySegmentSide(s.getStart());
    }

    public void rotate(float angle) {
        for (Point p : points)
            p.rotate(angle, this);
        for (Segment p : segments) {
            p.getStart().rotate(angle, this);
            p.getEnd().rotate(angle, this);
        }
    }

    public int getPointCount() {
        return points.size();
    }

    public Point getPoint(int pointNum) {
        return points.get(pointNum);
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    private void reSegment() {
        segments.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            segments.add(new Segment(points.get(i), points.get(i + 1)));
        }
        segments.add(new Segment(points.get(points.size() - 1), points.get(0)));
    }

    private static Point getCenter(Point[] vertexes) {
        Point centre = new Point(0, 0);
        for (Point vertex : vertexes) {
            centre.move(vertex);
        }
        return new Point(centre.getX() / vertexes.length, centre.getY() / vertexes.length);
    }

    public void move(float dx, float dy) {
        for (Segment s : segments)
            s.move(dx, dy);
        for (Point p : points)
            p.move(dx, dy);
        super.move(dx, dy);
    }

    public ConvexPolygon(Point[] vertexes) {
        super(getCenter(vertexes));
        Collections.addAll(points, vertexes);
        reSegment();
    }


}
