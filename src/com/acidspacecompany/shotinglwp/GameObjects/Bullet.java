package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Bullet extends Segment implements GameObject{

    private final float length;
    private final float angle;
    private float dx;
    private float dy;
    private boolean needable=true;
    private int rotateScaleMatrix=-1;

    public void dispose() {
        Graphic.cleanScaleMatrixID(rotateScaleMatrix);
    }

    public void setIsNoNeededMore(){
        needable=false;
    }

    @Override
    public boolean getIsNeeded(){
        return needable;
    }

    @Override
    public void reMatrix() {
        if (rotateScaleMatrix!=-1)
            Graphic.cleanScaleMatrixID(rotateScaleMatrix);
        rotateScaleMatrix =Graphic.getRotateScaleMatrixID(length, length, (float) Math.toDegrees(angle));
    }

    @Override
    public void update(float dt){
        float dxt=dx*dt;
        float dyt=dy*dt;
        move(dxt, dyt);
    }

    public void draw() {
            Graphic.bindRotateScaleMatrix(rotateScaleMatrix);
            Graphic.drawBitmap(getStart().getX(), getStart().getY());
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    public Bullet(float x1, float y1, float x2, float y2, float angle, float speed) {
        super(x1, y1, x2, y2);
        this.angle=angle;
        length= (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))*2;
        dx=speed*cos;
        dy=speed*sin;
        reMatrix();
    }
}
