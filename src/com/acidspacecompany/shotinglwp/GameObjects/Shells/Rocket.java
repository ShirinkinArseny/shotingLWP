package com.acidspacecompany.shotinglwp.GameObjects.Shells;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

public class Rocket extends Shell {

    private float length=0;
    private Point start;

    public void update(float dt) {
        if (getEnd().getSquaredDistanceToPoint(start)>=length) {
             setIsNoNeededMore();
        }
        super.update(dt);
    }

    public void dispose() {
        World.explode(this.getEnd());
        super.dispose();
    }

    public Rocket(float x1, float y1, float length, float angle, float speed, float distFly) {
        super(x1, y1, length, angle, speed);
        start=new Point(x1, y1);
        this.length=distFly;
    }
}