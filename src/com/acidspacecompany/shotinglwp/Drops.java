package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

public class Drops extends Point {

    private LinearTimeFunction tf;
    private LinearTimeFunction alpha;
    private float sin;
    private float cos;
    private float time=0;
    private float length;

    public boolean isNeedable(){
        return time<length;
    }

    public void update(float dt){
        tf.tick(dt);
        time+=dt;
    }

    public void draw() {/*   TODO
        drops.drawWithAlpha(getX(), getY(), tf.getValue(), sin, cos, alpha.getValue());*/
    }

    public Drops(float x, float y, float angle, float size, float length) {
        super(x, y);
        this.length=length;
        tf=new LinearTimeFunction(length, 0, size);
        alpha=new LinearTimeFunction(length, 1, 0);
        sin= (float) Math.sin(angle);
        cos= (float) Math.cos(angle);
    }
}
