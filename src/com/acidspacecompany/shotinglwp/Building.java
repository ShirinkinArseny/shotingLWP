package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Building extends Rectangle{

    public void draw(int id) {
        Graphic.drawBitmap(id, this);
    }

    public Building(float x, float y, int w, int h, float angle) {
        super(x, y, w, h);
        rotate(angle);
    }
}