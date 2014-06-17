package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;

import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.drawLine;

public class Man extends Point{

    private float width;
    private double angle;
    private float speed;
    private float cos;
    private float sin;
    private Point target;

    public void draw() {
        float angle=0;
        Point oldPos=new Point(getX()+width, getY());
        Point newPos;
        float deltaAngle= (float) (Math.PI/8);
        for (int i=0; i<=16; i++) {
            newPos=new Point(getX()+Math.cos(angle)*width, getY()+Math.sin(angle)*width);

            drawLine(oldPos.getX(), oldPos.getY(), newPos.getX(), newPos.getY(), 2, 0, 0, 0, 1);

            angle+=deltaAngle;
            oldPos=newPos;
        }
        drawLine(getX()+cos*width, getY()+sin*width, getX(), getY(), 2, 0, 0, 0, 1);
    }

    private void reAngle() {
        sin= (float) Math.sin(angle);
        cos= (float) Math.cos(angle);
    }

    public void roll(float anlge) {
        this.angle+=anlge;
        reAngle();
    }

    public void setTarget(Point p) {
        target=p;
        angle=Math.atan2(target.getY()-getY(), target.getX()-getX());
        reAngle();
    }

    public void move(float dt) {
        move(cos*dt*speed, sin*dt*speed);
    }

    public Man(float x, float y, float w, float speed) {
        super(x, y);
        target=this;
        width=w;
        this.speed=speed;
        angle=0;
        reAngle();
    }
}
