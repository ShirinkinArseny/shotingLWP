package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.World;

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
    private float time=0;
    private float timeLimit=0.3f;

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
        return health>=0;
    }

    private boolean disposed=false;
    public void dispose() {
        if (disposed) {
            BicycleDebugger.e("Man.dispose", "ALREADY DISPOSED!");
            new Exception().printStackTrace();
            System.exit(1);
        }
        disposed=true;
        Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Man");
        //BicycleDebugger.i("MAN.dispose", "Droppd " + rotateScaleMatrix);
    }

    public void setIsNoNeededMore(){
    }

    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    public void draw() {
        if (disposed) {
            BicycleDebugger.e("MAN.DRAW", "Man is already disposed, u CAN'T draw it!"); //todo
            new Exception().printStackTrace();
        }
        else {
        Graphic.bindRotateScaleMatrix(rotateScaleMatrix, "Man");
        Graphic.drawBitmap(getX(), getY());         }
    }

    public void setTarget(Point p) {
        angle= (float) Math.atan2(p.getY()-getY(), p.getX()-getX());
        cosSpeed= (float) (Math.cos(angle)*speed);
        sinSpeed= (float) (Math.sin(angle)*speed);
        reMatrix();
    }

    public void stopAndSetAngle(Point p) {
        angle= (float) Math.atan2(p.getY()-getY(), p.getX()-getX());
        sinSpeed=0;
        cosSpeed=0;
        reMatrix();
    }

    public void shot() {
        if (time<=0) {
            World.shot(this, angle);
            time+=timeLimit;
        }
    }

    public boolean getIsIntersect(Segment s) {
        if (widthPow2 < s.getSquaredLengthToLine(this)) return false;

        /*if (s.getAngleInStartIsAcute(this))
        return widthPow2 >=s.getStart().getSquaredDistanceToPoint(this);

        if (s.getAngleInEndIsAcute(this))
        return widthPow2 >=s.getEnd().getSquaredDistanceToPoint(this);*/

        return (s.getIsBetween(this));
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
        Segment s=new Segment(getX(), getY(), getX() + dx, getY() + dy);
        for (Building b: Buildings.getPotentialBarriers(getX() + dx, getY() + dy))
        {
              for (Segment s2: b.getSegments())  {
                  Point intr=s.getIntersection(s);
                  if (intr!=null) {
                      move(s2.getNormal().multiply(s2.getSide(this)));
                  }
              }
        }
        blockX= (int) (s.getEnd().getX());
        blockY= (int) (s.getEnd().getY());
        move(dx, dy);
        if (time>0) time-=dt;
    }

    public Man(float x, float y, int w, float speed) {
        super(x, y, w, w);
        width2=w/2;
        widthPow2=w*w*4;
        this.speed=speed;
        setTarget(new Point(50, 50));
    }

    public void reMatrix() {
        if (rotateScaleMatrix!=-1)
        Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Man");
        rotateScaleMatrix=Graphic.getRotateScaleMatrixID(width2, width2,
                (float) Math.toDegrees(angle), "Man");
        //BicycleDebugger.i("MAN.reMatrix", "Gotta "+rotateScaleMatrix);
    }
}