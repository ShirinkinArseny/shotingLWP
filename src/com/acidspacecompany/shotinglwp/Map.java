package com.acidspacecompany.shotinglwp;

import android.util.Log;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.LinkedList;
import java.util.Random;

public class Map {

    private LinkedList<Man> men=new LinkedList<>();
    private LinkedList<Bullet> bulls=new LinkedList<>();
    private LinkedList<Drops> drops=new LinkedList<>();

    private static final Random rnd=new Random();

    public Map() {
        for (int i=0; i<100; i++)
            men.add(new Man(rnd.nextInt(800), rnd.nextInt(480), 5, 50));
    }

    public void update(float dt) {
        for(Man m: men) {
            m.move(dt);
            if (rnd.nextInt(1000)==0)
                m.setTarget(new Point(rnd.nextInt(800), rnd.nextInt(480)));
        }

        for(Bullet b: bulls) {
            b.update(dt);
            for(Man m: men) {
               if (m.getIsIntersect(b)) {
                   ///
                      b.destroy();
                   Log.i("LOL", String.valueOf(b.getAngle()));
                   drops.add(new Drops(m.getX(), m.getY(), b.getAngle(), 60f, 0.2f));
                   ///
               }
                else
                   if (b.getStart().getX()<0 || b.getStart().getX()>800 || b.getStart().getY()<0 || b.getStart().getY()>480)
                       b.destroy();
            }
        }

        for(int i=0; i<bulls.size(); i++) {
            if (!bulls.get(i).isNeedable()) {
                bulls.remove(i);
            }
        }

        for(Drops d: drops) {
            d.update(dt);
        }
        for(int i=0; i<drops.size(); i++) {
            if (!drops.get(i).isNeedable()) {
                drops.remove(i);
            }
        }


        if (rnd.nextInt(10)==0) {
            float x=400;
            float y=240;
            double angle=Math.PI*2*rnd.nextFloat();

            bulls.add(new Bullet(x, y, (float) (x + Math.cos(angle) * 10), (float) (y + Math.sin(angle) * 10), 500));
        }
    }

    public void draw() {
        Man.startDraw();
        for(Man m: men)
            m.draw();

        Drops.startDraw();
        for(Drops m: drops)
            m.draw();

        Graphic.startDrawLines(0, 0, 0, 1, 1.3f);
        for(Bullet m: bulls) {
            m.draw();
        }
        Graphic.endDrawLines();
    }
}