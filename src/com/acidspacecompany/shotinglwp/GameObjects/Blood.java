package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

import java.util.Random;

public class Blood extends Point implements GameObject{

    private LinearTimeFunction alpha;
    private float time=0;
    private float length;
    private int rotateScaleMatrix=-1;
    private static final Random rnd=new Random();

    @Override
    public void reMatrix() {

    }

    public void update(float dt){
        alpha.tick(dt);
        time+=dt;
    }

    public void draw() {
        float t=alpha.getValue();
        Graphic.setThresholdParams(t/2, t, (t+0.5f)/1.5f);
        Graphic.bindRotateScaleMatrix(rotateScaleMatrix);
        Graphic.drawBitmap(getX(), getY());
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {
        Graphic.cleanScaleMatrixID(rotateScaleMatrix);
    }

    @Override
    public void setIsNoNeededMore(){
    }

    @Override
    public boolean getIsNeeded() {
        return time<length;
    }

    public Blood(float x, float y, float size, float length) {
        super(x, y);
        this.length=length;
        alpha=new LinearTimeFunction(length, 0, 1);
        rotateScaleMatrix=Graphic.getRotateScaleMatrixID(size, size,
                rnd.nextFloat()*360);
    }
}
