package com.acidspacecompany.shotinglwp.GameObjects.Effects;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.LinkedList;

import static com.acidspacecompany.shotinglwp.DefaultUpdateDrawMethods.*;
import static com.acidspacecompany.shotinglwp.DefaultUpdateDrawMethods.drawGameObjectLayer;

public class EffectsLayer {

    private static LinkedList<Blood> blood = new LinkedList<>();
    private static LinkedList<Explosion> explosions = new LinkedList<>();
    private static LinkedList<Smoke> smoke = new LinkedList<>();
    private static LinkedList<Extinction> lights = new LinkedList<>();
    private static LinkedList<Extinction> explosionLights = new LinkedList<>();


    public static void makeBlood(Point b, float size) {
        blood.add(new Blood(b.getX(), b.getY(), size, 1f));
    }

    public static void makeLight(Point b, float size) {
        lights.add(new Extinction(b.getX(), b.getY(), size, 0.2f));
    }

    public static void makeSelection(Point b, float size) {
        lights.add(new Extinction(b.getX(), b.getY(), size*4, 5f));
        lights.add(new Extinction(b.getX(), b.getY(), size*2, 5f));
        lights.add(new Extinction(b.getX(), b.getY(), size, 5f));
    }

    public static void makeExplosionLight(Point b, float size) {
        explosionLights.add(new Extinction(b.getX(), b.getY(), size, 0.4f));
    }

    public static void makeExplosion(Point b, float size) {
        explosions.add(new Explosion(b.getX(), b.getY(), size, 0.2f));
    }

    public static void makeSmoke(Point b, float size) {
        smoke.add(new Smoke(b.getX(), b.getY(), size, 1f));
    }

    public static void reMatrix() {
        for (Blood m : blood) {
            m.reMatrix();
        }
        for (Extinction m : lights) {
            m.reMatrix();
        }
        for (Explosion m : explosions) {
            m.reMatrix();
        }
        for (Smoke m : smoke) {
            m.reMatrix();
        }
    }

    public static void update(float dt) {
        /*---   blood   ---*/
        updateGameObjects(blood, dt);
        removeUnusedGameObjects(blood);
        /*---   lights   ---*/
        updateGameObjects(lights, dt);
        removeUnusedGameObjects(lights);
        /*---   explosionlights   ---*/
        updateGameObjects(explosionLights, dt);
        removeUnusedGameObjects(explosionLights);
        /*---   smoke   ---*/
        updateGameObjects(smoke, dt);
        removeUnusedGameObjects(smoke);
        /*---   explosions   ---*/
        updateGameObjects(explosions, dt);
        removeUnusedGameObjects(explosions);
    }

    public static void fxDrawBlood(int bloodID) {
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
        drawGameObjectLayer(blood, bloodID);
    }

    public static void fxDrawLights(int lightsID, int explosionLightsID) {
        drawGameObjectLayer(lights, lightsID);
        drawGameObjectLayer(explosionLights, explosionLightsID);
    }

    public static void fxDrawExplosions(int explosionID) {
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
        drawGameObjectLayer(explosions, explosionID);
    }

    public static void fxDrawSmoke(int smokeID) {
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
        drawGameObjectLayer(smoke, smokeID);
    }
}
