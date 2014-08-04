package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ManAI;
import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.World;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Man extends Rectangle implements GameObject{

    private ManAI brain=new ManAI(this);
    private float width2;
    private float widthPow2;
    private float angle;
    private float speed;
    private float cosSpeed;
    private float sinSpeed;
    private float health=1f;
    private int blockX=0;
    private int blockY=0;
    private LinkedList<Man> visibleMan=new LinkedList<>();
    private int rotateScaleMatrix=-1;
    private float bulletTime =0;
    private float bulletTimeLimit =0.1f;
    private float rocketTime =0;
    private float rocketTimeLimit =10f;

    public ManAI getBrains() {     //thnx for fr8 IDE
        return brain;
    }

    private Man thisMan=this;
    private Comparator<? super Man> comparator=new Comparator<Man>() {
        @Override
        public int compare(Man man, Man t1) {
            return Float.compare(man.getSquaredDistanceToPoint(thisMan), t1.getSquaredDistanceToPoint(thisMan));
        }
    };

    public void cleanVisibility(){
        visibleMan.clear();
    }

    public void addVisibleMan(Man m) {
        visibleMan.add(m);
        Collections.sort(visibleMan, comparator);
    }

    public Man getVisibleMan() {
        for (int i=0; i<visibleMan.size(); i++) {
            if (!visibleMan.get(i).getIsNeeded()) {
                visibleMan.remove(0);
                i--;
            }
            else
            if (!Buildings.getConnected(visibleMan.get(i).getX(), visibleMan.get(i).getY(),
                    getX(), getY())) {
                visibleMan.remove(0);
                i--;
            }
            else return visibleMan.get(0);
        }
        return null;
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
        health=-1;
    }

    public void prepareToDraw() {
    }

    public void draw() {
        if (disposed) {
            //BicycleDebugger.e("MAN.DRAW", "Man is already disposed, u CAN'T draw it!"); //todo
            //new Exception().printStackTrace();
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

    public boolean canShot() {
        return bulletTime <=0;
    }

    public boolean canLaunchRocket() {
        return rocketTime <=0;
    }

    public void shot() {
        if (canShot()) {
            World.shot(this, angle);
            bulletTime += bulletTimeLimit;
        }
    }

    public void launchRocket(Point to) {
        if (canLaunchRocket()) {
            World.launchRocket(this, angle, to);
            rocketTime += rocketTimeLimit;
        }
    }

    public boolean getIsIntersect(Segment s) {
        //System.out.println("Accepted distance: "+width2);
        //System.out.println("Distance: "+s.getLengthToLine(this));

        if (widthPow2 < s.getLengthToLine(this)){
            //System.out.println("Accepted distance: "+widthPow2);
            //System.out.println("Distance: " + s.getLengthToLine(this));
            return false;
        }

        /*if (s.getAngleInStartIsAcute(this))
        return widthPow2 >=s.getStart().getSquaredDistanceToPoint(this);

        if (s.getAngleInEndIsAcute(this))
        return widthPow2 >=s.getEnd().getSquaredDistanceToPoint(this);*/
        return true;

        /*if (s.getIsBetween(this)) return true;
        //System.out.println("Not between");
        if (s.getStart().getSquaredDistanceToPoint(this)<widthPow2) return true;
        if (s.getEnd().getSquaredDistanceToPoint(this)<widthPow2) return true;
        return false;*/
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
        if (bulletTime >0) bulletTime -=dt;
        if (rocketTime >0) rocketTime -=dt;
    }

    public Man(float x, float y, int w, float speed) {
        super(x, y, w, w);
        width2=w/2;
        widthPow2=width2*width2;
        this.speed=speed;
        setTarget(new Point(50, 50));
    }

    @Deprecated
    public Man(float x, float y, int w, float speed, float dummy) {
        super(x, y, w, w);
        width2=w/2;
        widthPow2=width2*width2;
        this.speed=speed;
    }

    public String toString() {
        return super.toString()+" hp: "+health+" visibility: "+(getVisibleMan()!=null);
    }

    public void reMatrix() {
        if (rotateScaleMatrix!=-1)
        Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Man");
        rotateScaleMatrix=Graphic.getRotateScaleMatrixID(width2, width2,
                (float) Math.toDegrees(angle), "Man");
        //BicycleDebugger.i("MAN.reMatrix", "Gotta "+rotateScaleMatrix);
    }
}