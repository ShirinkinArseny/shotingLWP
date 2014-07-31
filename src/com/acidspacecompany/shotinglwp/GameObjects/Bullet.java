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
        Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Bullet");
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
            Graphic.cleanScaleMatrixID(rotateScaleMatrix, "Bullet");
        rotateScaleMatrix =Graphic.getRotateScaleMatrixID(
                length, length, (float) Math.toDegrees(angle), "Bullet");
    }

    @Override
    public void update(float dt){
        move(dx*dt, dy*dt);
    }

    public void draw() {
            Graphic.bindRotateScaleMatrix(rotateScaleMatrix, "Bullet");
            Graphic.drawBitmap(getStart().getX(), getStart().getY());
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    public Bullet(float x1, float y1, float length, float angle, float speed) {
        super(x1, y1, (float)(x1 + Math.cos(angle) * length), (float)(y1 + Math.sin(angle) * length));
        this.angle=angle;
        this.length=length;
        dx=speed*cos;
        dy=speed*sin;
        reMatrix();
    }
}
