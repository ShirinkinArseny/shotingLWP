package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.*;

import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.*;

public class World {

    private static final float lineWidth=1.3f;
    private static int displayWidth=800;
    private static int displayHeight=480;

    private boolean active = true;//is working
    private long lastTime;
    private static LinkedList<Man> men=new LinkedList<>();
    private static LinkedList<Bullet> bulls=new LinkedList<>();
    private static LinkedList<Drops> drops=new LinkedList<>();
    private static final Random rnd=new Random();

    public static void shot(float x, float y, float angle) {
        bulls.add(new Bullet(x, y, (float) (x + Math.cos(angle) * 10), (float) (y + Math.sin(angle) * 10), 500));
    }

    public static void drop(Bullet b) {
        drops.add(new Drops(b.getEnd().getX(), b.getEnd().getY(), b.getAngle(), 60f, 0.2f));
    }

    public World() {
        for (int i=0; i<100; i++)
            men.add(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), 5, 50));
    }

    public void init() {
        Man.init(lineWidth);
        Drops.init(lineWidth);
    }

    public void pausePainting() {
        active = false;
    }

    public void resumePainting() {
        active = true;
        lastTime = System.currentTimeMillis();
    }

    public void stopPainting() {
        active = false;
    }

    public void updateAndDraw() {
        if (active) {
            long cTime = System.currentTimeMillis();
            float delta = (cTime - lastTime) / 1000f;
            lastTime = cTime;
            update(delta);
            Graphic.startDraw();
            draw();
        }
    }

    public void setSurface(int width, int height) {
        Graphic.resize(width, height);
    }

    private void updateMen(float dt){
        for(Man m: men) {
            m.move(dt);
            if (rnd.nextInt(1000)==0)
                m.setTarget(new Point(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight)));
        }
    }

    private void updateBullets(float dt) {
        for(Bullet b: bulls) {
            b.update(dt);
        }
    }

    private void checkManAndBulletsForIntersection() {
        for(Bullet b: bulls) {
            if (b.getStart().getX()<0 || b.getStart().getX()>displayWidth || b.getStart().getY()<0 || b.getStart().getY()>displayHeight)
                b.destroy();
            else
            for(Man m: men) {
                if (m.getIsIntersect(b)) {
                    b.destroy();
                    drop(b);
                    break;
                }
            }
        }
    }

    private void removeUnusedBullets() {
        for(int i=0; i<bulls.size(); i++) {
            if (!bulls.get(i).isNeedable()) {
                bulls.remove(i);
            }
        }
    }

    private void updateDrops(float dt) {
        for(Drops d: drops) {
            d.update(dt);
        }
    }

    private void removeUnusedDrops() {
        for(int i=0; i<drops.size(); i++) {
            if (!drops.get(i).isNeedable()) {
                drops.remove(i);
            }
        }
    }

    private float angle=0;
    private void spawnNewUnits() {
        if (rnd.nextInt(3)==0) {
            shot(400, 240, angle);
        angle+=0.05f;
        }
    }

    public void update(float dt) {
        updateMen(dt);
        updateBullets(dt);
        checkManAndBulletsForIntersection();
        removeUnusedBullets();
        updateDrops(dt);
        removeUnusedDrops();
        spawnNewUnits();
    }

    private void drawMenLayer() {
        Man.startDraw();
        for(Man m: men)
            m.draw();
    }

    private void drawDropsLayer() {
        Drops.startDraw();
        for(Drops m: drops)
            m.draw();
    }

    private void drawBulletsLayer() {
        Graphic.startDrawLines(0, 0, 0, 1, lineWidth);
        for(Bullet m: bulls) {
            m.draw();
        }
        Graphic.endDrawLines();
    }

    private void draw() {
        startDraw();
        drawMenLayer();
        drawDropsLayer();
        drawBulletsLayer();
        end();
    }
}