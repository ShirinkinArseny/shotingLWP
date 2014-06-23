package com.acidspacecompany.shotinglwp.Geometry;

import java.util.ArrayList;

public class ConvexPolygon {

    private ArrayList<Segment> segments=new ArrayList<>();
    private ArrayList<Point> points=new ArrayList<>();
    private Point centre = new Point(0, 0);

    public boolean intersect(ConvexPolygon c) {
        for (Point p: points)
            if (c.contains(p)) return true;
        for (Point p: c.points)
            if (contains(p)) return true;
        return false;
    }

    public boolean contains(Point point) {
        float D =  segments.get(0).getD(point);
        float newD;
        for (int i = 1; i < segments.size(); i++) {
            newD=segments.get(i).getD(point);
            if (newD>=0 && D<=0 || newD<=0 && D>=0) return false;
            D=newD;
        }
        return true;
    }

    public int getPointCount() {
        return points.size();
    }

    public Point getPoint(int pointNum) {
        return points.get(pointNum);
    }

    public Point getCentre() {
        return centre;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    private void reSegment() {
        segments.clear();
        for (int i=0; i<points.size()-1; i++) {
            segments.add(new Segment(points.get(i),points.get(i+1)));
        }
        segments.add(new Segment(points.get(points.size()-1),points.get(0)));
    }

    private void reCenter() {
        centre.setPosition(0, 0);
        for (Point vertex : points) {
            centre.move(vertex);
        }
        centre=new Point(centre.getX() / points.size(), centre.getY() / points.size());
    }

    public ConvexPolygon(Point[] vertexes) {
        for (Point p: vertexes)
                points.add(p);
        reCenter();
        reSegment();
    }


}
