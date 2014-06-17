package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.LinkedList;
import java.util.Random;

public class Map {

    private LinkedList<Man> men=new LinkedList<>();
    private LinkedList<Segment> segms=new LinkedList<>();

    private static final Random rnd=new Random();

    public Map() {
        for (int i=0; i<100; i++)
            men.add(new Man(rnd.nextInt(800), rnd.nextInt(480), 5, 50));

        for (int i=0; i<40; i++) {
            float x=rnd.nextInt(800);
            float y=rnd.nextInt(480);
            segms.add(new Segment(x, y, x+rnd.nextInt(60), y+rnd.nextInt(60)));
        }

    }

    public void update(float dt) {
        for(Man m: men) {
            m.move(dt, segms);
            if (rnd.nextInt(1000)==0)
                m.setTarget(new Point(rnd.nextInt(800), rnd.nextInt(480)));
        }
    }

    public void draw() {
        Man.startDrawPrimitives();
        for(Man m: men)
            m.draw();
        Graphic.startDrawLines(0, 0, 0, 1, 1.3f);
        for(Segment m: segms)
            Graphic.drawLine(m.getStart(), m.getEnd());
        Graphic.endDrawLines();
    }
}