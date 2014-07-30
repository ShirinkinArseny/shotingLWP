package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Building extends Rectangle implements GameObject{

    private int matrix;
    private float angle;

    public void draw() {
        Graphic.bindResultMatrix(matrix);
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {
        Graphic.bindColor(1, 1, 1, 1);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean getIsNeeded() {
        return false;
    }

    public void reMatrix() {
        Graphic.cleanResultMatrixID(matrix);
        matrix=Graphic.getResultMatrixID(getX(), getY(), getW(), getH(), angle);
    }

    @Override
    public void update(float dt) {

    }

    public Building(float x, float y, int w, int h, float angle) {
        super(x, y, w, h, angle);
        this.angle= 90+(float) Math.toDegrees(angle);
        matrix=Graphic.getResultMatrixID(x, y, w, h, this.angle);
    }
}