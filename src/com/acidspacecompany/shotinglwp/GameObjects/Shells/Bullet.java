package com.acidspacecompany.shotinglwp.GameObjects.Shells;

public class Bullet extends Shell {

    public void dispose() {
        //makeSmoke(this.getEnd(), World.bulletLength/2);
        super.dispose();
    }

    public Bullet(float x1, float y1, float length, float angle, float speed) {
        super(x1, y1, length, angle, speed);
    }
}
