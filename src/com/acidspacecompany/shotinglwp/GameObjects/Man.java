package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Man extends Rectangle implements GameObject{

    private float widthPow2;
    private float width2;
    private float angle;
    private float speed;
    private float cosSpeed;
    private float sinSpeed;
    private float health=1f;
    private int blockX=0;
    private int blockY=0;
    private Man visibleMan;
    private int rotateScaleMatrix=-1;

    public void cleanVisibility(){
        visibleMan=null;
    }

    public void addVisibleMan(Man m) {
        visibleMan=m;
    }

    public Man getVisibleMan() {
        return visibleMan;
    }

    public void damage(float damage) {
        health-=damage;
    }

    public boolean getIsNeeded() {
        return health>0;
    }

    public void dispose() {
        Graphic.cleanScaleMatrixID(rotateScaleMatrix);}

    public void setIsNoNeededMore(){
        health=-1;
    }

    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    public void draw() {
        Graphic.bindRotateScaleMatrix(rotateScaleMatrix);
        Graphic.drawBitmap(getX(), getY());
    }

    public void setTarget(Point p) {
        angle= (float) Math.atan2(p.getY()-getY(), p.getX()-getX());
        cosSpeed= (float) (Math.cos(angle)*speed);
        sinSpeed= (float) (Math.sin(angle)*speed);
        reMatrix();
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

    public void update(float dt) {
        float dx=dt*cosSpeed;
        float dy=dt*sinSpeed;
        int newBlockX= Buildings.getXBlock(getX() + dx);
        int newBlockY= Buildings.getYBlock(getY() + dy);
        if (Buildings.getContainsPotentialBarriers(blockX, blockY)) {
            return;
        }
        blockX=newBlockX;
        blockY=newBlockY;
        move(dx, dy);
    }

    public Man(float x, float y, int w, float speed) {
        super(x, y, w, w);
        width2=w/2;
        widthPow2=w*w;
        this.speed=speed;
        setTarget(new Point(50, 50));
    }

    public void reMatrix() {
        if (rotateScaleMatrix!=-1)
        Graphic.cleanScaleMatrixID(rotateScaleMatrix);
        rotateScaleMatrix=Graphic.getRotateScaleMatrixID(width2, width2, (float) Math.toDegrees(angle));
    }
}