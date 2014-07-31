package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

import java.util.Random;

public class Explosion extends Point implements GameObject{

    private LinearTimeFunction alpha;
    private float time=0;
    private float length;
    private float size;
    private int resultMatrix=-1;
    private static final Random rnd=new Random();

    @Override
    public void reMatrix() {
        if (resultMatrix!=-1)
            Graphic.cleanResultMatrixID(resultMatrix, "Explosion");
        resultMatrix=Graphic.getResultMatrixID(getX(), getY(), size, size,
                rnd.nextFloat() * 360, "Explosion");
    }

    public void update(float dt){
        alpha.tick(dt);
        time+=dt;
    }

    public void draw() {
        float t=1-alpha.getValue();
        Graphic.setThresholdParams(t/2, t, 1);
        Graphic.bindResultMatrix(resultMatrix, "Explosion");
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {
        Graphic.cleanResultMatrixID(resultMatrix, "Explosion");
    }

    @Override
    public void setIsNoNeededMore(){
    }

    @Override
    public boolean getIsNeeded() {
        return time<length;
    }

    public Explosion(float x, float y, float size, float length) {
        super(x, y);
        this.length=length;
        this.size=size;
        alpha=new LinearTimeFunction(length, 0, 1);
        reMatrix();
    }
}
