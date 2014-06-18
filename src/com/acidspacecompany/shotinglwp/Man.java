package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives.LinePrimitive;

public class Man extends Point{

    private float width;
    private float widthPow2;
    private float angle;
    private float angleRadian;
    private float speed;
    private float cosSpeed;
    private float sinSpeed;
    private Point target;
    private static LinePrimitive round;

    public static void startDraw() {
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

        round=new LinePrimitive(vertexes, 0, 0, 0, 1, 1.3f);
    }

    public void draw() {
        round.draw(getX(), getY(), width, angleRadian);
    }

    private void reAngle() {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        cosSpeed= cos *speed;
        sinSpeed= sin *speed;
        angleRadian= (float) Math.toDegrees(angle);
    }

    public void setTarget(Point p) {
        target=p;
        angle= (float) Math.atan2(target.getY()-getY(), target.getX()-getX());
        reAngle();
    }

    public boolean getIsIntersect(Segment s) {
        return widthPow2>= getSquaredDistanceToSegment(s);
    }

    public void move(float dt) {
        float dx=dt*cosSpeed;
        float dy=dt*sinSpeed;
        move(dx, dy);
    }

    public Man(float x, float y, float w, float speed) {
        super(x, y);
        target=this;
        width=w;
        widthPow2=w*w;
        this.speed=speed;
        angle=0;
        reAngle();
    }
}