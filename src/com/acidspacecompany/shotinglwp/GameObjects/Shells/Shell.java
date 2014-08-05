package com.acidspacecompany.shotinglwp.GameObjects.Shells;

import com.acidspacecompany.shotinglwp.GameObjects.Building;
import com.acidspacecompany.shotinglwp.GameObjects.Buildings;
import com.acidspacecompany.shotinglwp.GameObjects.GameObject;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public abstract class Shell extends Segment implements GameObject {

    private Segment motion;
    private float dx;
    private float dy;
    private float length;
    private float length_2;
    private float angle;
    private int rotateScaleMatrix=-1;
    private boolean needable=true;
    static int snum=1;
    int num;

    public void dispose() {
        Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Shell");
        //BicycleDebugger.i("BULLET.dispose", "Droppd " + rotateScaleMatrix);
    }

    public void reMatrix() {
        if (rotateScaleMatrix!=-1)
            Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Shell");
        rotateScaleMatrix =Graphic.getRotateScaleMatrixID(
                length_2, length_2, (float) Math.toDegrees(angle), "Shell");
        //BicycleDebugger.i("BULLET.reMatrix", "Gotta "+rotateScaleMatrix);
    }

    public Segment getMotion() {
        return motion;
    }

    @Override
    public void update(float dt){

        float lx=getStart().getX();
        float ly=getStart().getY();
        move(dx*dt, dy*dt);

        float nx=getEnd().getX();
        float ny=getEnd().getY();
        motion=new Segment(lx, ly, nx, ny);

        for (Building b: Buildings.getPotentialBarriers(lx, ly)) {
             if (b.intersectsBySegmentIntersection(motion)) {
                 setIsNoNeededMore();
                 break;
             }
        }

    }

    public void move(float delta) {
        move(delta*cos, delta*sin);
    }


    public void setIsNoNeededMore(){
        needable=false;
    }

    @Override
    public boolean getIsNeeded(){
        return needable;
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }


    public void draw() {
        Graphic.bindRotateScaleMatrix(rotateScaleMatrix, "Shell");
        Graphic.drawBitmap(getStart().getX(), getStart().getY());
    }

    public Shell(float x, float y, float length, float angle, float speed) {
        super(x, y,
            (float)(x + Math.cos(angle) * length),
            (float)(y + Math.sin(angle) * length)
        );
        this.length=length;
        length_2=length/2;
        this.angle=angle;
        dx=speed*cos;
        dy=speed*sin;

        //move(cos* World.manSize, sin*World.manSize);
        reMatrix();

        num=snum;
        snum++;
    }
}
