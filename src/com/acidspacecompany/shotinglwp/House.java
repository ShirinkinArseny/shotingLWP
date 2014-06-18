package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitive;

import java.util.LinkedList;

public class House extends Point{

    private float width;
    private float widthPow2;
    private float angle;
    private float angleRadian;
    private float speed;
    private float cos;
    private float sin;
    private Point target;
    private static Primitive round;

    public static void startDrawPrimitives() {
        round.startDraw();
    }

    public static void init() {
        float[] vertexes=new float[72];
        float deltaAngle= (float) (Math.PI/8);
        float angle= 0;
        for (int i=0; i<=16; i++) {
            vertexes[i*4]= (float) Math.cos(angle);
            vertexes[i*4+1]= (float) Math.sin(angle);
            angle+=deltaAngle;
            vertexes[i*4+2]= (float) Math.cos(angle);
            vertexes[i*4+3]= (float) Math.sin(angle);
        }
        vertexes[64]= vertexes[62];
        vertexes[65]= vertexes[63];
        vertexes[66]= vertexes[0];
        vertexes[67]= vertexes[1];
        vertexes[68]= 0;
        vertexes[69]= 0;
        vertexes[70]= 1;
        vertexes[71]= 0;

        round=new Primitive(vertexes, 0, 0, 0, 1, 1.3f);
    }

    public void draw() {
        round.draw(getX(), getY(), width, angleRadian);
    }

    private void reAngle() {
        sin= (float) Math.sin(angle);
        cos= (float) Math.cos(angle);
        angleRadian= (float) Math.toDegrees(angle);
    }

    public void roll(float anlge) {
        this.angle+=anlge;
        reAngle();
    }

    public void setTarget(Point p) {
        target=p;
        angle= (float) Math.atan2(target.getY()-getY(), target.getX()-getX());
        reAngle();
    }

    private boolean getIsIntersect(Segment s) {
        return width>= getSquaredDistanceToSegment(s);
    }

    public void move(float dt, LinkedList<Segment> segms) {
        float dx=cos*dt*speed;
        float dy=sin*dt*speed;
        Point m=this.add(dx, dy);
        for (Segment s: segms) {
            if (widthPow2 >= m.getSquaredDistanceToSegment(s))
                return;
        }
        move(cos*dt*speed, sin*dt*speed);
    }

    public House(float x, float y, float w, float speed) {
        super(x, y);
        target=this;
        width=w;
        widthPow2=w*w;
        this.speed=speed;
        angle=0;
        reAngle();
    }
}