package com.acidspacecompany.shotinglwp.GameObjects.Effects;

import com.acidspacecompany.shotinglwp.GameObjects.GameObject;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

import java.util.Random;

public class Extinction extends Point implements GameObject {

    private LinearTimeFunction alpha;
    private float time=0;
    private float length;
    private float size;
    private int resultMatrix=-1;
    private static final Random rnd=new Random();

    @Override
    public void reMatrix() {
        if (resultMatrix!=-1)
            Graphic.cleanResultMatrixID(resultMatrix, "Extinction");
        resultMatrix=Graphic.getResultMatrixID(getX(), getY(), size, size,
                rnd.nextFloat() * 360, "Extinction");
    }

    public void update(float dt){
        alpha.tick(dt);
        time+=dt;
    }

    public void draw() {
        Graphic.bindColor(1, 1, 1, alpha.getValue());
        Graphic.bindResultMatrix(resultMatrix, "Extinction");
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {

    }

    @Override
    public void dispose() {
        Graphic.cleanResultMatrixID(resultMatrix, "Extinction");
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
        reMatrix();
    }
}
