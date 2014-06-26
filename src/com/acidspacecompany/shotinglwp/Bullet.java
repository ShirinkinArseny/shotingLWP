package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

public class Bullet extends Segment {

    private float dx;
    private float dy;
    private boolean needable=true;

    public void destroy() {
        needable=false;
    }

    public boolean isNeedable(){
        return needable;
    }

    public void update(float dt){
        float dxt=dx*dt;
        float dyt=dy*dt;
        move(dxt, dyt);
    }

    public void draw() {/*                      TODO
        Graphic.drawLine(getStart(), getEnd());*/
    }

    public Bullet(float x1, float y1, float x2, float y2, float speed) {
        super(x1, y1, x2, y2);
        dx=speed*cos;
        dy=speed*sin;
    }
}
