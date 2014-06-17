package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.*;

import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.end;
import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.endDrawLines;
import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.startDrawLines;

public class World {

    private boolean active = true;//is working
    private long lastTime;
    private Map m=new Map();

    public World() {
        Man.init();
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

    public void run() {
        lastTime = System.currentTimeMillis();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateAndDraw();
            }
        }, 0, 10);
    }

    public void setSurface(int width, int height) {
        Graphic.resize(width, height);
    }

    private void update(float dt) {
        m.update(dt);
    }

    private void draw() {
        startDrawLines(0, 0, 0, 1, 1.3f);
        m.draw();
        endDrawLines();
        end();
    }
}