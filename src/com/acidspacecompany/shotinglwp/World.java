package com.acidspacecompany.shotinglwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.AIBase;
import com.acidspacecompany.shotinglwp.GameObjects.Background;
import com.acidspacecompany.shotinglwp.GameObjects.Building;
import com.acidspacecompany.shotinglwp.GameObjects.Buildings;
import com.acidspacecompany.shotinglwp.GameObjects.Effects.EffectsLayer;
import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.GameObjects.Shells.Bullet;
import com.acidspacecompany.shotinglwp.GameObjects.Shells.Rocket;
import com.acidspacecompany.shotinglwp.GameObjects.Shells.Shell;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Network.NetworkDebugger;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.*;

import static com.acidspacecompany.shotinglwp.DefaultUpdateDrawMethods.*;
import static com.acidspacecompany.shotinglwp.GameObjects.Effects.EffectsLayer.*;

public class World {

    private static int displayWidth = 800;
    private static int displayHeight = 480;

    private static float timer = 0;
    private static final float timerLimit = 2f;

    private static boolean playing=true;/* TODO: DEBUG */

    private static boolean active = true;//is working
    private static long lastTime;
    private static Background background=new Background();
    private static LinkedList<Man> men = new LinkedList<>();
    private static LinkedList<Man>[] teamedMen = new LinkedList[]{new LinkedList(), new LinkedList()};
    private static LinkedList<Man>[] visibleMen = new LinkedList[]{new LinkedList(), new LinkedList()};
    private static LinkedList<Bullet> bullets = new LinkedList<>();
    private static LinkedList<Rocket> rockets = new LinkedList<>();
    private static AIBase[] ais=new AIBase[2];
    private static final Random rnd = new Random();
    private static final float squaredVisibleDistance = 40000;
    private static Resources res;
    private static float pictureSizeCoef;
    private static int backgroundID;
    private static int redID;
    private static int blueID;
    private static int houseID;
    private static int bulletID;
    private static int rocketID;
    private static int bloodID;
    private static int lightsID;
    private static int explosionLightsID;
    private static int explosionID;
    private static int smokeID;
    public static float shellDamage =0.1f;
    public static float bulletLength=80;
    public static float bulletSpeed=500;
    public static float bloodSize=20;
    public static float lightSize=30;
    public static float manSize=18;
    public static float manSpeed=40;
    public static float explosionRadius=35;
    public static float explosionRadius4=explosionRadius*4;
    public static float explosionRadius13= (int) (explosionRadius*1.3f);
    public static float explosionRadius_2=explosionRadius/2;
    public static float explosionRadius_4=explosionRadius/4;
    public static float explosionRadius_4_13=(int) (explosionRadius_4*1.3f);
    private static NetworkDebugger nwd=new NetworkDebugger();

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
        makeExplosionLight(p, explosionRadius4);
        makeSmoke(p, explosionRadius13);
        for (int i=0; i<10; i++)  {

            Point tmpPoint=new Point(
                    p.getX()-explosionRadius_2+rnd.nextInt((int) explosionRadius),
                    p.getY()-explosionRadius_2+rnd.nextInt((int) explosionRadius));

            makeExplosion(tmpPoint, explosionRadius_4);
            makeSmoke(tmpPoint, explosionRadius_4_13);

        }

    }

    public static void shot(Point p, float angle) {
        float delta=(rnd.nextFloat()-0.5f)/6;
        Bullet b = new Bullet(p.getX(), p.getY(), bulletLength, angle+delta, bulletSpeed);
        b.move(manSize);
        makeLight(p, World.lightSize);
        bullets.add(b);
    }

    public static void launchRocket(Point p, float angle, Point to) {
        float delta=(rnd.nextFloat()-0.5f)/12;
        Rocket b = new Rocket(p.getX(), p.getY(), bulletLength, angle+delta, bulletSpeed, to.getSquaredDistanceToPoint(p));
        b.move(manSize);
        makeLight(p, World.lightSize);
        rockets.add(b);
    }

    public static void addMan() {
        addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), manSize, manSpeed), rnd.nextInt(2));
    }

    private static void addMan(Man m, int team) {
        men.add(m);
        teamedMen[team].add(m);
        ais[team].manAdded(m);
    }

    public static void init(Context context) {
        res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef = Math.max(metrics.widthPixels, metrics.heightPixels) / 800f;
        resize(metrics.widthPixels, metrics.heightPixels);

        ais[0]=new AIBase(teamedMen[0], visibleMen[1]);
        ais[1]=new AIBase(teamedMen[1], visibleMen[0]);


        bulletLength=getScaledValue(80);
        bulletSpeed=getScaledValue(500);
        bloodSize=getScaledValue(20);
        lightSize=getScaledValue(30);
        manSize=getScaledValue(18);
        manSpeed=getScaledValue(40);
        explosionRadius=getScaledValue(35);
        explosionRadius4=explosionRadius*4;
        explosionRadius13= (int) (explosionRadius*1.3f);
        explosionRadius_2=explosionRadius/2;
        explosionRadius_4=explosionRadius/4;
        explosionRadius_4_13=(int) (explosionRadius_4*1.3f);

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

        Bitmap rocket = getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.rocket),
                (int) getScaledValue(256));
        rocketID = Graphic.genTexture(rocket);

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


        for (int i = 0; i < 100; i++)
            addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), manSize, manSpeed), i % 2);

        Buildings.init(displayWidth, displayHeight);
        Buildings.addBarrier(new Building(getScaledValue(240),
                getScaledValue(80), getScaledValue(60), getScaledValue(30), 0));
        Buildings.addBarrier(new Building(getScaledValue(240),
                getScaledValue(200), getScaledValue(60), getScaledValue(30), 0));
        Buildings.addBarrier(new Building(getScaledValue(240),
                getScaledValue(320), getScaledValue(60), getScaledValue(30), 0));
        Buildings.addBarrier(new Building(getScaledValue(560),
                getScaledValue(80), getScaledValue(60), getScaledValue(30), 0));
        Buildings.addBarrier(new Building(getScaledValue(560),
                getScaledValue(200), getScaledValue(60), getScaledValue(30), 0));
        Buildings.addBarrier(new Building(getScaledValue(560),
                getScaledValue(320), getScaledValue(60), getScaledValue(30), 0));
        Buildings.finishAddingBarriers();
    }

    public static void pausePainting() {
        active = false;
    }

    public static void resumePainting() {
        active = true;
        lastTime = System.currentTimeMillis();
    }

    public static void stopPainting() {
        active = false;
    }

    public static void updateAndDraw() {
        long cTime = System.currentTimeMillis();
        float delta = (cTime - lastTime) / 1000f;
        lastTime = cTime;
        update(delta);
        draw();
    }

    public static void resize(int width, int height) {
        Graphic.resize(width, height);
        displayWidth=width;
        displayHeight=height;
        for (Man m : men) {
            m.reMatrix();
        }
        for (Building m : Buildings.getBarriers()) {
            m.reMatrix();
        }
        for (Bullet m : bullets) {
            m.reMatrix();
        }
        EffectsLayer.reMatrix();
        background.reMatrix();
    }

    //Пузырёк в продакшоне :3
    private static void sortPeople() {
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

    private static void checkManAndShellForIntersection(List bullets) {
        for (Object b2 : bullets) {
            Shell b=(Shell)b2;

            if (b.getStart().getX() < 0 || b.getStart().getX() > displayWidth
                    || b.getStart().getY() < 0 || b.getStart().getY() > displayHeight)
                b.setIsNoNeededMore();

            else {

                if (b.getDx() > 0) {
                    int index = 0;//getNearestMenIndex(b.getStart().getX());          TODO
                    int target = men.size();//getNearestMenIndex(b.getEnd().getX());  TODO
                    for (int i = index; i < target; i++) {
                        if (men.get(i).getIsIntersect(b.getMotion())) {
                            men.get(i).damage(shellDamage);
                            b.setIsNoNeededMore();
                            break;
                        }
                    }
                } else {
                    int target = 0;//getNearestMenIndex(b.getStart().getX());         TODO
                    int index = men.size();//getNearestMenIndex(b.getEnd().getX());   TODO
                    for (int i = target; i < index; i++) {
                        if (men.get(i).getIsIntersect(b.getMotion())) {
                            men.get(i).damage(shellDamage);
                            b.setIsNoNeededMore();
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean getIsVisible(Man m1, Man m2) {
        return m1.getSquaredDistanceToPoint(m2) <= squaredVisibleDistance &&
                Buildings.getConnected(m1.getX(), m1.getY(), m2.getX(), m2.getY());
    }

    private static void updateVisibility() {
        visibleMen[0].clear();
        visibleMen[1].clear();

        for (Man m1 : teamedMen[0]) m1.cleanVisibility();
        for (Man m2 : teamedMen[1]) m2.cleanVisibility();

        for (Man m1 : teamedMen[0]) {
            for (Man m2 : teamedMen[1]) {
                    if (getIsVisible(m1, m2)) {

                            visibleMen[0].add(m1);
                            m1.addVisibleMan(m2);

                            visibleMen[1].add(m2);
                            m2.addVisibleMan(m1);
                    }
            }
        }
    }

    //private float angle = 0;
    public static void updateAI(float dt) {
        //if (rnd.nextInt(3) == 0) {
        //    shot(400, 240, angle);
        //}
        //angle += 0.01f;
        //if (rnd.nextInt(10) == 0)
        //    addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), manSize, manSpeed), rnd.nextInt(2));


        timer += dt;
        if (timer > timerLimit) {
            timer -= timerLimit;
            updateVisibility();
        }

        for (AIBase a: ais) {
            a.update(dt);
        }
    }

    public static void update(float dt) {
        if (playing) {
        /* TODO: DEBUG */
        /*---   bullets   ---*/
            updateGameObjects(bullets, dt);
            removeUnusedGameObjects(bullets);
            checkManAndShellForIntersection(bullets);
        /*---   rockets   ---*/
            updateGameObjects(rockets, dt);
            removeUnusedGameObjects(rockets);
            checkManAndShellForIntersection(rockets);
        /*---   spawn   ---*/
            updateAI(dt);
        /*---   men   ---*/
            updateGameObjects(men, dt);
            removeUnusedGameObjects(men);
            sortPeople();
            removeUnusedGameObjectsWithoutDispose(teamedMen[0]);
            removeUnusedGameObjectsWithoutDispose(teamedMen[1]);
            removeUnusedGameObjectsWithoutDispose(visibleMen[0]);
            removeUnusedGameObjectsWithoutDispose(visibleMen[1]);

        }

        /* ГРАФОН */
        EffectsLayer.update(dt);
    }

    private static void draw() {


        Graphic.startDraw();
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);

        Graphic.bindBitmap(backgroundID);
        background.draw();

        fxDrawBlood(bloodID);

        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);

        fxDrawLights(lightsID, explosionLightsID);

            drawGameObjectLayer(bullets, bulletID);
            drawGameObjectLayer(rockets, rocketID);

            drawGameObjectLayer(teamedMen[0], redID);
            drawGameObjectLayer(teamedMen[1], blueID);

        fxDrawExplosions(explosionID);

        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
            drawGameObjectLayer(Buildings.getBarriers(), houseID);

        fxDrawSmoke(smokeID);

        Graphic.end();
    }

    /* TODO: DEBUG */

    public static void killAll() {
        for (Man m: men)
            m.setIsNoNeededMore();
    }

    public static void setPhysicIsWorking(boolean value) {
        playing =value;
    }

    public static void selectUnit(int num) {
        if (num>=0 && num<=men.size()) {
            makeSelection(men.get(num), lightSize);
        }
    }

    public static void printUnit(int num) {
        if (num>=0 && num<=men.size()) {
            Log.i("PrintUnit ", num + ": " + men.get(num));
        }
    }

    public static void printBullets() {
        Log.i("PrintBullet ", "TOTAL "+bullets.size()+" BULLETS");
        for (Bullet bullet : bullets) {
            Log.i("PrintBullet ", bullet.getMotion().getStart() + " >> " + bullet.getMotion().getEnd());
        }
    }
}