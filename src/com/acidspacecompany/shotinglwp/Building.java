package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Building extends Rectangle{

    private int matrix;
    private float angle;

    public void draw() {
        Graphic.bindResultMatrix(matrix);
        Graphic.drawBitmap();
    }

    public void reMatrix() {
        Graphic.cleanResultMatrixID(matrix);
        matrix=Graphic.getResultMatrixID(getX(), getY(), getW(), getH(), angle);
    }

    public Building(float x, float y, int w, int h, float angle) {
        super(x, y, w, h);
        this.angle= (float) Math.toDegrees(angle);
        rotate(angle);
        matrix=Graphic.getResultMatrixID(x, y, w, h, this.angle);
    }
}