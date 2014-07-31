package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Building extends Rectangle implements GameObject{

    private int matrix=-1;
    private float angle;

    public void draw() {
        Graphic.bindResultMatrix(matrix, "Building");
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {
        Graphic.cleanResultMatrixID(matrix, "Building");
    }

    @Override
    public void setIsNoNeededMore(){
    }

    @Override
    public boolean getIsNeeded() {
        return true;
    }

    public void reMatrix() {
        if (matrix!=-1)
        Graphic.cleanResultMatrixID(matrix, "Building");
        matrix=Graphic.getResultMatrixID(getX(), getY(), getW(), getH(), angle, "Building");
    }

    @Override
    public void update(float dt) {

    }

    public Building(float x, float y, int w, int h, float angle) {
        super(x, y, w, h, angle);
        this.angle= 90+(float) Math.toDegrees(angle);
        reMatrix();
    }
}