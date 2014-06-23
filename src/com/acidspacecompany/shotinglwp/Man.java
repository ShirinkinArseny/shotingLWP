package com.acidspacecompany.shotinglwp;

import android.util.Log;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives.LinePrimitive;

public class Man extends Point{

    private float width;
    private float widthPow2;
    private float angle;
    private float speed;
    private float cos;
    private float sin;
    private float cosSpeed;
    private float sinSpeed;
    private float health=1f;
    private int blockX=0;
    private int blockY=0;
    private static LinePrimitive round;

    public static void startDraw() {
        round.startDraw();
    }

    public static void init(float width) {
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

        round=new LinePrimitive(vertexes, 0, 0, 0, 1, width);
    }

    public void damage(float damage) {
        health-=damage;
    }

    public boolean getIsNeeded() {
        return health>0;
    }

    public void draw() {
        round.draw(getX(), getY(), width, sin, cos);
    }

    private void reAngle() {
        sin = (float) Math.sin(angle);
        cos = (float) Math.cos(angle);
        cosSpeed= cos *speed;
        sinSpeed= sin *speed;
    }

    public void setTarget(Point p) {
        angle= (float) Math.atan2(p.getY()-getY(), p.getX()-getX());
        reAngle();
    }

    public boolean getIsIntersect(Segment s) {
        if (widthPow2 < s.getSquaredLengthToLine(this)) return false;

        if (s.getAngleInStartIsAcute(this))
        return widthPow2 >=s.getStart().getSquaredDistanceToPoint(this);

        if (s.getAngleInEndIsAcute(this))
        return widthPow2 >=s.getEnd().getSquaredDistanceToPoint(this);

        return true;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void move(float dt) {
        float dx=dt*cosSpeed;
        float dy=dt*sinSpeed;
        move(dx, dy);
        blockX=Barriers.getXBlock(getX());
        blockY=Barriers.getYBlock(getY());
        for (Building b: Barriers.getPotentialBarriers(blockX, blockY)) {
            if (b.contains(this))
            {
                move(-dx, -dy);
                return;
            }
        }
    }

    public Man(float x, float y, float w, float speed) {
        super(x, y);
        width=w;
        widthPow2=w*w;
        this.speed=speed;
        setTarget(new Point(400, 240));
    }
}