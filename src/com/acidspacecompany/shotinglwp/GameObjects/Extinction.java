package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

import java.util.Random;

public class Extinction extends Point implements GameObject{

    private LinearTimeFunction alpha;
    private float time=0;
    private float length;
    private float size;
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
        Graphic.bindColor(1, 1, 1, alpha.getValue());
        Graphic.bindRotateScaleMatrix(rotateScaleMatrix);
        Graphic.drawBitmap(getX(), getY());
    }

    @Override
    public void prepareToDraw() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void setIsNoNeededMore(){
    }

    @Override
    public boolean getIsNeeded() {
        return time<length;
    }

    public Extinction(float x, float y, float size, float length) {
        super(x, y);
        this.length=length;
        this.size=size;
        alpha=new LinearTimeFunction(length, 1, 0);
        rotateScaleMatrix=Graphic.getRotateScaleMatrixID(size, size,
                rnd.nextFloat()*360);
    }
}
