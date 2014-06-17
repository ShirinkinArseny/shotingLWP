package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;

import java.util.LinkedList;
import java.util.Random;

public class Map {

    private LinkedList<Man> men=new LinkedList<>();

    private static final Random rnd=new Random();

    public Map() {
        for (int i=0; i<100; i++)
        men.add(new Man(50, 50, 5, 50));
    }

    public void update(float dt) {
        for(Man m: men) {
            m.move(dt);
            if (rnd.nextInt(1000)==0)
                m.setTarget(new Point(rnd.nextInt(800), rnd.nextInt(480)));
        }
    }

    public void draw() {
        for(Man m: men)
            m.draw();
    }
}
