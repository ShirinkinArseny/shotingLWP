package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Bullet extends Segment implements GameObject{

    private float dx;
    private float dy;
    private boolean needable=true;
    private int rotateScaleMatrix=-1;

    public void dispose() {
        if (!needable) {
            BicycleDebugger.e("Bullet.dispose", "REDISPOSE!");
            try {
                throw new Exception("Redispose, printing stack trace");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
        needable=false;
        Graphic.cleanScaleMatrixID(rotateScaleMatrix);   }
    }

    @Override
    public boolean getIsNeeded(){
        return needable;
    }

    @Override
    public void reMatrix() {
    }

    @Override
    public void update(float dt){
        float dxt=dx*dt;
        float dyt=dy*dt;
        move(dxt, dyt);
    }

    public void draw() {
        try {
            Graphic.bindRotateScaleMatrix(rotateScaleMatrix);
            Graphic.drawBitmap(getStart().getX(), getStart().getY());
        }
        catch (Exception e) {
            BicycleDebugger.e("Bullet.draw", "damn, excption: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    public Bullet(float x1, float y1, float x2, float y2, float angle, float speed) {
        super(x1, y1, x2, y2);
        float length= (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        length*=2;
        rotateScaleMatrix =Graphic.getRotateScaleMatrixID(length, length, (float) Math.toDegrees(angle));
        dx=speed*cos;
        dy=speed*sin;
    }
}
