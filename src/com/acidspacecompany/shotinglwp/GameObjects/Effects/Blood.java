package com.acidspacecompany.shotinglwp.GameObjects.Effects;

import com.acidspacecompany.shotinglwp.GameObjects.GameObject;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

import java.util.Random;

public class Blood extends Point implements GameObject {

    private LinearTimeFunction alpha;
    private float time=0;
    private float length;
    private float size;
    private int resultMatrix=-1;
    private static final Random rnd=new Random();

    @Override
    public void reMatrix() {
        if (resultMatrix!=-1)
            Graphic.cleanResultMatrixID(resultMatrix, "Blood");
        resultMatrix=Graphic.getResultMatrixID(getX(), getY(), size, size,
                rnd.nextFloat() * 360, "Blood");
    }

    public void update(float dt){
        alpha.tick(dt);
        time+=dt;
    }

    public void draw() {
        float t=alpha.getValue();
        Graphic.setThresholdParams(t/2, t, (t+0.5f)/1.5f);
        Graphic.bindResultMatrix(resultMatrix, "Blood");
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {
        Graphic.cleanResultMatrixID(resultMatrix, "Blood");
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
        this.size=size;
        alpha=new LinearTimeFunction(length, 0, 1);
        reMatrix();
    }
}
