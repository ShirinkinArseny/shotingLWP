package com.acidspacecompany.shotinglwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private static Background background=new Background();
    private static LinkedList<Man> men = new LinkedList<>();
    private static LinkedList<Man>[] teamedMen = new LinkedList[]{new LinkedList(), new LinkedList()};
    private static LinkedList<Bullet> bulls = new LinkedList<>();
    private static LinkedList<Blood> blood = new LinkedList<>();
    private static LinkedList<Explosion> explosions = new LinkedList<>();
    private static LinkedList<Smoke> smoke = new LinkedList<>();
    private static LinkedList<Extinction> lights = new LinkedList<>();
    private static LinkedList<Extinction> explosionLights = new LinkedList<>();
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
    private int explosionLightsID;
    private int explosionID;
    private int smokeID;
    private static final float bulletDamage=0.3f;
    private static final float bulletLength=40;
    private static final float bulletSpeed=500;
    private static final float bloodSize=30;
    private static final float lightSize=60;
    private static final int manSize=20;
    private static final int manSpeed=50;
    private static final int explosionRadius=40;
    private static final int explosionRadius4=explosionRadius*4;
    private static final int explosionRadius13= (int) (explosionRadius*1.3f);
    private static final int explosionRadius_2=explosionRadius/2;
    private static final int explosionRadius_4=explosionRadius/4;
    private static final int explosionRadius_4_13=(int) (explosionRadius_4*1.3f);

    public static int getDisplayWidth() {
        return displayWidth;
    }

    public static int getDisplayHeight() {
        return displayHeight;
    }

    private static Bitmap getScaledBitmap(Bitmap b, int size) {
        return Bitmap.createScaledBitmap(b, size, size, true);
    }

    public static float getScaledValue(float value) {
        return pictureSizeCoef * value;
    }


    public static void explode(Point p) {
        for (Man m: men) {
            double d=m.getDistanceToPoint(p);
            if (d>explosionRadius) continue;
            m.damage((float) (1 - d / explosionRadius));
        }
        explosionLights.add(new Extinction(p.getX(), p.getY(), explosionRadius4, 0.4f));
        smoke.add(new Smoke(p.getX(), p.getY(), explosionRadius13, 1f));
        for (int i=0; i<10; i++)  {

            float x=p.getX()-explosionRadius_2+rnd.nextInt(explosionRadius);
            float y=p.getY()-explosionRadius_2+rnd.nextInt(explosionRadius);

            explosions.add(new Explosion(x, y, explosionRadius_4, 0.2f));
            smoke.add(new Smoke(x, y,explosionRadius_4_13, 1f));

        }

    }

    public static void shot(float x, float y, float angle) {
        Bullet b = new Bullet(x, y, bulletLength, angle+(rnd.nextFloat()-0.5f)/4, bulletSpeed);
        makeLight(b);
        bulls.add(b);
    }

    public static void shot(Point p, float angle) {
        shot(p.getX(), p.getY(), angle);
    }

    public void makeBlood(Bullet b) {
        blood.add(new Blood(b.getEnd().getX(), b.getEnd().getY(), bloodSize, 1f));
    }

    public static void makeLight(Bullet b) {
        lights.add(new Extinction(b.getStart().getX(), b.getStart().getY(), lightSize, 0.2f));
    }

    private void addMan(Man m, int team) {
        men.add(m);
        teamedMen[team].add(m);
    }

    public World(Context context) {
        res = context.getResources();

        for (int i = 0; i < 100; i++)
            addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), manSize, manSpeed), i % 2);

        Buildings.init(displayWidth, displayHeight);
        Buildings.addBarrier(new Building(240,
                80, 60, 30, 0));
        Buildings.addBarrier(new Building(240,
                200, 60, 30, 0));
        Buildings.addBarrier(new Building(240,
                320, 60, 30, 0));
        Buildings.addBarrier(new Building(560,
                80, 60, 30, 0));
        Buildings.addBarrier(new Building(560,
                200, 60, 30, 0));
        Buildings.addBarrier(new Building(560,
                320, 60, 30, 0));
        Buildings.finishAddingBarriers();
    }

    public void init() {
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef = Math.max(metrics.widthPixels, metrics.heightPixels) / 800f;

        //todo: sizes
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

        Bitmap explosionLight = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.explosionlight),
                (int) getScaledValue(256));
        explosionLightsID = Graphic.genTexture(explosionLight);

        Bitmap explosion = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.explosion),
                (int) getScaledValue(256));
        explosionID = Graphic.genTexture(explosion);

        Bitmap smoke = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.smoke),
                (int) getScaledValue(256));
        smokeID = Graphic.genTexture(smoke);
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
        draw();
    }

    public void resize(int width, int height) {
        Graphic.resize(width, height);
        displayWidth=width;
        displayHeight=height;
        for (Man m : men) {
            m.reMatrix();
        }
        for (Building m : Buildings.getBarriers()) {
            m.reMatrix();
        }
        background.reMatrix();
        for (Blood m : blood) {
            m.reMatrix();
        }
        for (Extinction m : lights) {
            m.reMatrix();
        }
        for (Bullet m : bulls) {
            m.reMatrix();
        }
        for (Explosion m : explosions) {
            m.reMatrix();
        }
        for (Smoke m : smoke) {
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
                    explode(b.getEnd());
                    b.setIsNoNeededMore();
                    break;
                }
            }

            if (b.getIsNeeded())
            for (ConvexPolygon bu : Buildings.getPotentialBarriers(b.getEnd().getX(), b.getEnd().getY())) {
                if (bu.containsBySegmentSide(b.getEnd())) {
                    explode(b.getEnd());
                    b.setIsNoNeededMore();
                    break;
                }
            }
        }
    }

    private void checkManAndBulletsForIntersection() {
        for (Bullet b : bulls) {

            if (b.getStart().getX() < 0 || b.getStart().getX() > displayWidth
                    || b.getStart().getY() < 0 || b.getStart().getY() > displayHeight)
                b.setIsNoNeededMore();

            else {

                if (b.getDx() > 0) {
                    int index = getNearestMenIndex(b.getStart().getX()-bulletLength);
                    int target = getNearestMenIndex(b.getEnd().getX()+bulletLength);
                    for (int i = index; i < target; i++) {
                        if (men.get(i).getIsIntersect(b)) {
                            men.get(i).damage(bulletDamage);
                            b.setIsNoNeededMore();
                            makeBlood(b);
                            break;
                        }
                    }
                } else {
                    int target = getNearestMenIndex(b.getStart().getX()-bulletLength);
                    int index = getNearestMenIndex(b.getEnd().getX()+bulletLength);
                    for (int i = target; i < index; i++) {
                        if (men.get(i).getIsIntersect(b)) {
                            men.get(i).damage(bulletDamage);
                            b.setIsNoNeededMore();
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
                ((GameObject) objects.get(i)).dispose();
                objects.remove(i);
                //if (i>0) i--;
                i=0;//OVERKILL MOTHERFUCKER
            }
        }
    }

    private void removeUnusedGameObjectsWithoutDispose(List objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (!((GameObject) objects.get(i)).getIsNeeded()) {
                objects.remove(i);
                if (i>0) i--;
            }
        }
    }

    private void updateGameObjects(List objects, float dt) {
        for (Object go : objects) {
            ((GameObject) go).update(dt);
        }
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

    //private float angle = 0;
    public void updateAI(float dt) {
        //if (rnd.nextInt(3) == 0) {
        //    shot(400, 240, angle);
        //}
        //angle += 0.01f;
        if (rnd.nextInt(30) == 0)
            addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), 20, 50), rnd.nextInt(2));


        timer += dt;
        if (timer > timerLimit) {
            timer -= timerLimit;
            updateVisibility();
        }
        for (Man m: men) {
            if (rnd.nextInt(50) == 0)
                m.setTarget(new Point(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight)));
            else
                if (m.getVisibleMan()!=null) {
                    if (rnd.nextInt(25)==0) {
                        m.setTarget(m.getVisibleMan());
                    }
                    else {
                        m.stopAndSetAngle(m.getVisibleMan());
                        m.shot();
                    }
                }
        }
    }

    public void update(float dt) {
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
        /*---   explosionlights   ---*/
        updateGameObjects(explosionLights, dt);
        removeUnusedGameObjects(explosionLights);
        /*---   explosions   ---*/
        updateGameObjects(explosions, dt);
        removeUnusedGameObjects(explosions);
        /*---   smoke   ---*/
        updateGameObjects(smoke, dt);
        removeUnusedGameObjects(smoke);
        /*---   spawn   ---*/
        updateAI(dt);
        /*---   men   ---*/
        updateGameObjects(men, dt);
        sortPeople();
        removeUnusedGameObjects(men);
        for (Man m: men) {
            if (!m.getIsNeeded()) {
                Log.e("Update0", "DEAD ALIVE MAN, FUUUUUCK!");
                new Exception().printStackTrace();
                Log.e("Update0", m.toString());
                Log.e("Update0", m.toString());
                System.exit(1);
            }
        }
        removeUnusedGameObjectsWithoutDispose(teamedMen[0]);
        removeUnusedGameObjectsWithoutDispose(teamedMen[1]);
        for (Man m: men) {
            if (!m.getIsNeeded()) {
                Log.e("Update", "DEAD ALIVE MAN, FUUUUUCK!");
                new Exception().printStackTrace();
                Log.e("Update", m.toString());
                Log.e("Update", m.toString());
                System.exit(1);
            }
        }
    }

    private void drawBgLayer() {
        Graphic.bindBitmap(backgroundID);
        background.draw();
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
        Graphic.startDraw();
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
            drawBgLayer();
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
            drawGameObjectLayer(blood, bloodID);
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
            drawGameObjectLayer(lights, lightsID);
            drawGameObjectLayer(explosionLights, explosionLightsID);
            drawGameObjectLayer(bulls, bulletID);
            drawGameObjectLayer(teamedMen[0], redID);
            drawGameObjectLayer(teamedMen[1], blueID);
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
            drawGameObjectLayer(explosions, explosionID);
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
            drawGameObjectLayer(Buildings.getBarriers(), houseID);
        Graphic.begin(Graphic.Mode.DRAW_THRESHOLD_BITMAP);
            drawGameObjectLayer(smoke, smokeID);
        Graphic.end();
    }
}