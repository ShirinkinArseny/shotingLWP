package com.acidspacecompany.shotinglwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import com.acidspacecompany.shotinglwp.GameObjects.*;
import com.acidspacecompany.shotinglwp.Geometry.ConvexPolygon;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.*;

public class World {

    private static int displayWidth = 800;
    private static int displayHeight = 480;

    private static float timer = 0;
    private static final float timerLimit = 2f;

    private boolean active = true;//is working
    private long lastTime;
    private static LinkedList<Man> men = new LinkedList<>();
    private static LinkedList<Man>[] teamedMen = new LinkedList[]{new LinkedList(), new LinkedList()};
    private static LinkedList<Bullet> bulls = new LinkedList<>();
    private static LinkedList<Blood> blood = new LinkedList<>();
    private static LinkedList<Extinction> lights = new LinkedList<>();
    private static HashSet<Man>[] visibleMen = new HashSet[]{new HashSet(), new HashSet()};
    private static final Random rnd = new Random();
    private static final float squaredVisibleDistance = 40000;
    private static Resources res;
    private static float pictureSizeCoef;
    private int backgroundID;
    private int redID;
    private int blueID;
    private int houseID;
    private int bulletID;
    private int bloodID;
    private int lightsID;

    private static Bitmap getScaledBitmap(Bitmap b, int size) {
        return Bitmap.createScaledBitmap(b, size, size, true);
    }

    public static float getScaledValue(float value) {
        return pictureSizeCoef * value;
    }

    public static void shot(float x, float y, float angle) {
        Bullet b = new Bullet(x, y, (float) (x + Math.cos(angle) * 20), (float) (y + Math.sin(angle) * 20), angle, 500);
        makeLight(b);
        bulls.add(b);
    }

    public static void makeBlood(Bullet b) {
        blood.add(new Blood(b.getEnd().getX(), b.getEnd().getY(), 60f, 1f));
    }

    public static void makeLight(Bullet b) {
        lights.add(new Extinction(b.getStart().getX(), b.getStart().getY(), 60f, 0.2f));
    }

    private void addMan(Man m, int team) {
        men.add(m);
        teamedMen[team].add(m);
    }

    public World(Context context) {
        res = context.getResources();

        for (int i = 0; i < 100; i++)
            addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), 20, 50), i % 2);

        Buildings.init(displayWidth, displayHeight);
        Buildings.addBarrier(new Building(270,
                250, 125, 12, (float) Math.PI / 2 + 0.3f));
        Buildings.finishAddingBarriers();
    }

    public void init() {
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef = Math.max(metrics.widthPixels, metrics.heightPixels) / 1100f;

        Bitmap background = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.background),
                (int) getScaledValue(256));
        backgroundID = Graphic.genTexture(background);

        Bitmap red = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.red),
                (int) getScaledValue(256));
        redID = Graphic.genTexture(red);

        Bitmap blue = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.blue),
                (int) getScaledValue(256));
        blueID = Graphic.genTexture(blue);

        Bitmap house = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.house),
                (int) getScaledValue(256));
        houseID = Graphic.genTexture(house);

        Bitmap bullet = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bullet),
                (int) getScaledValue(256));
        bulletID = Graphic.genTexture(bullet);

        Bitmap blood = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.blood),
                (int) getScaledValue(256));
        bloodID = Graphic.genTexture(blood);

        Bitmap light = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.light),
                (int) getScaledValue(256));
        lightsID = Graphic.genTexture(light);
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
        long cTime = System.currentTimeMillis();
        float delta = (cTime - lastTime) / 1000f;
        lastTime = cTime;
        update(delta);
        Graphic.startDraw();
        draw();
    }

    public void resize(int width, int height) {
        Graphic.resize(width, height);
        for (Man m : men) {
            m.reMatrix();
        }
        for (Building m : Buildings.getBarriers()) {
            m.reMatrix();
        }
    }

    //Пузырёк в продакшоне :3
    private void sortPeople() {
        for (int i = 0; i < men.size(); i++) {
            boolean swapped = false;
            for (int j = 0; j < men.size() - 1; j++)
                if (men.get(j).getX() > men.get(j + 1).getX()) {
                    Collections.swap(men, j, j + 1);
                    swapped = true;
                }
            if (!swapped) break;
        }
    }

    private int getNearestMenIndex(float x) {
        int leftIndex = 0;
        int rightIndex = men.size() - 1;
        while (rightIndex - leftIndex > 1) {
            int midIndex = (leftIndex + rightIndex) / 2;
            if (men.get(midIndex).getX() < x)
                leftIndex = midIndex;
            else rightIndex = midIndex;
        }
        return leftIndex;
    }

    private void checkBuildingsAndBulletsForIntersection() {
        for (Bullet b : bulls) {
            if (b.getIsNeeded())
            for (ConvexPolygon bu : Buildings.getPotentialBarriers(b.getStart().getX(), b.getStart().getY())) {
                if (bu.containsBySegmentSide(b.getStart())) {
                    b.dispose();
                    //todo: drop something
                    break;
                }
            }

            if (b.getIsNeeded())
            for (ConvexPolygon bu : Buildings.getPotentialBarriers(b.getEnd().getX(), b.getEnd().getY())) {
                if (bu.containsBySegmentSide(b.getEnd())) {
                    b.dispose();
                    //todo: drop something
                    break;
                }
            }
        }
    }

    private void checkManAndBulletsForIntersection() {
        for (Bullet b : bulls)
            if (b.getIsNeeded()) {
            if (b.getStart().getX() < 0 || b.getStart().getX() > displayWidth || b.getStart().getY() < 0 || b.getStart().getY() > displayHeight)
                b.dispose();
            else {
                int index = getNearestMenIndex(b.getStart().getX());
                int target = getNearestMenIndex(b.getEnd().getX());

                if (b.getDx() > 0) {
                    index++;
                    target = Math.min(target + 1, men.size() - 1);
                    for (int i = index; i < target; i++) {
                        if (men.get(i).getIsIntersect(b)) {
                            b.dispose();
                            makeBlood(b);
                            break;
                        }
                    }
                } else {
                    for (int i = index; i >= target; i--) {
                        if (men.get(i).getIsIntersect(b)) {
                            b.dispose();
                            makeBlood(b);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void removeUnusedGameObjects(List objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (!((GameObject) objects.get(i)).getIsNeeded()) {
                objects.remove(i);
            }
        }
    }

    private void updateGameObjects(List objects, float dt) {
        for (Object go : objects) {
            ((GameObject) go).update(dt);
        }
    }

    private float angle = 0;

    private void spawnNewUnits() {
        if (rnd.nextInt(3) == 0) {
            shot(400, 240, angle);
        }
        angle += 0.05f;
    }

    private boolean getIsVisible(Man m1, Man m2) {
        return m1.getSquaredDistanceToPoint(m2) <= squaredVisibleDistance &&
                Buildings.getConnected(m1.getBlockX(), m1.getBlockY(), m2.getBlockX(), m2.getBlockY());
    }

    private void updateVisibility() {
        visibleMen[0].clear();
        visibleMen[1].clear();

        boolean[] visibility1 = new boolean[teamedMen[0].size()];
        boolean[] visibility2 = new boolean[teamedMen[1].size()];

        for (Man m1 : teamedMen[0]) m1.cleanVisibility();
        for (Man m2 : teamedMen[1]) m2.cleanVisibility();

        int num1 = 0;
        int num2;

        for (Man m1 : teamedMen[0]) {
            num2 = 0;
            for (Man m2 : teamedMen[1]) {
                if (!(visibility1[num1] && visibility2[num2]))
                    if (getIsVisible(m1, m2)) {

                        if (!visibility1[num1]) {
                            visibleMen[0].add(m1);
                            visibility1[num1] = true;
                            m1.addVisibleMan(m2);
                        }

                        if (!visibility2[num2]) {
                            visibleMen[1].add(m2);
                            visibility2[num2] = true;
                            m2.addVisibleMan(m1);
                        }

                        visibility1[num1] = true;
                    }
                num2++;
            }
            num1++;
        }
    }

    public void updateAI(float dt) {
        timer += dt;
        if (timer > timerLimit) {
            timer -= timerLimit;
            updateVisibility();
        }
        for (Man m: men)
        if (rnd.nextInt(100) == 0)
            m.setTarget(new Point(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight)));
    }

    public void update(float dt) {
        /*---   men   ---*/
        updateGameObjects(men, dt);
        removeUnusedGameObjects(men);
        removeUnusedGameObjects(teamedMen[0]);
        removeUnusedGameObjects(teamedMen[1]);
        sortPeople();
        /*---   bullets   ---*/
        updateGameObjects(bulls, dt);
        checkManAndBulletsForIntersection();
        checkBuildingsAndBulletsForIntersection();
        removeUnusedGameObjects(bulls);
        /*---   blood   ---*/
        updateGameObjects(blood, dt);
        removeUnusedGameObjects(blood);
        /*---   lights   ---*/
        updateGameObjects(lights, dt);
        removeUnusedGameObjects(lights);
        /*---   spawn   ---*/
        spawnNewUnits();
        updateAI(dt);
    }

    private void drawBgLayer() {
        Graphic.bindBitmap(backgroundID);     //todo: matrix
        Graphic.drawBitmap(0, 0, displayWidth, displayHeight, 0);
    }

    private void drawGameObjectLayer(List objects, int textureID) {
        if (!objects.isEmpty()) {
            Graphic.bindBitmap(textureID);
            ((GameObject) objects.get(0)).prepareToDraw();
            for (Object go : objects) {
                ((GameObject) go).draw();
            }
        }
    }

    private void draw() {
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
        drawBgLayer();
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
        drawGameObjectLayer(blood, bloodID);
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
        drawGameObjectLayer(lights, lightsID);
        drawGameObjectLayer(teamedMen[0], redID);
        drawGameObjectLayer(teamedMen[1], blueID);
        drawGameObjectLayer(bulls, bulletID);
        drawGameObjectLayer(Buildings.getBarriers(), houseID);
        Graphic.end();
    }
}