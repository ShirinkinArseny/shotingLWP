package com.acidspacecompany.shotinglwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import com.acidspacecompany.shotinglwp.Geometry.ConvexPolygon;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.*;

import static com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic.*;

public class World {

    private static final float lineWidth = 1.3f;
    private static int displayWidth = 800;
    private static int displayHeight = 480;

    private static float timer = 0;
    private static final float timerLimit = 2f;

    private boolean active = true;//is working
    private long lastTime;
    private static LinkedList<Man> men = new LinkedList<>();
    private static LinkedList<Man>[] teamedMen = new LinkedList[]{new LinkedList(), new LinkedList()};
    private static LinkedList<Bullet> bulls = new LinkedList<>();
    private static LinkedList<Drops> drops = new LinkedList<>();
    private static HashSet<Man>[] visibleMen = new HashSet[]{new HashSet(), new HashSet()};
    private static final Random rnd = new Random();
    private static final float squaredVisibleDistance=40000;
    private static Resources res;
    private static float pictureSizeCoef;
    private int backgroundID;
    private int redID;
    private int blueID;
    private int houseID;

    private static Bitmap getScaledBitmap(Bitmap b, int size) {
        return Bitmap.createScaledBitmap(b, size, size, true);
    }

    private static Bitmap getScaledResource(Resources res, int id, int size) {
        return getScaledBitmap(BitmapFactory.decodeResource(res, id), (int) (size*pictureSizeCoef));
    }

    public static float getScaledValue(float value) {
        return pictureSizeCoef*value;
    }

    public static void shot(float x, float y, float angle) {
        Bullet b = new Bullet(x, y, (float) (x + Math.cos(angle) * 10), (float) (y + Math.sin(angle) * 10), 500);
        bulls.add(b);
        drop(b, false);
    }

    private static final float Pi = (float) Math.PI;
    public static void drop(Bullet b, boolean reverse) {
        if (reverse)
            drops.add(new Drops(b.getEnd().getX(), b.getEnd().getY(), b.getAngle() + Pi, 60f, 0.2f));
        else
            drops.add(new Drops(b.getEnd().getX(), b.getEnd().getY(), b.getAngle(), 60f, 0.2f));
    }

    private void addMan(Man m, int team) {
        men.add(m);
        teamedMen[team].add(m);
    }

    public World(Context context) {
        for (int i = 0; i < 100; i++)
            addMan(new Man(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight), 5, 50), i % 2);

        Barriers.init(displayWidth, displayHeight);
        for (int i = 0; i < 10; i++)
            for (int j=0; j<2; j++){
                Barriers.addBarrier(new Building(50+i*70,
                        50+j*200, 150, 50, (float) Math.PI/2));

            }
        Barriers.finishAddingBarriers();
        res=context.getResources();
    }

    public void init() {
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef=Math.max(metrics.widthPixels, metrics.heightPixels)/1100f;

        Bitmap background=getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.background),
                (int) getScaledValue(256));
        backgroundID=Graphic.genTexture(background);

        Bitmap red=getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.red),
                (int) getScaledValue(256));
        redID=Graphic.genTexture(red);

        Bitmap blue=getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.blue),
                (int) getScaledValue(256));
        blueID=Graphic.genTexture(blue);

        Bitmap house=getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.house),
                (int) getScaledValue(256));
        houseID=Graphic.genTexture(house);

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

    private void updateMen(float dt) {
        for (Man m : men) {
            m.move(dt);
            if (rnd.nextInt(1000) == 0)
                m.setTarget(new Point(rnd.nextInt(displayWidth), rnd.nextInt(displayHeight)));
        }
    }

    private void removeUnusedMen() {
        for (int i = 0; i < men.size(); i++) {
            if (!men.get(i).getIsNeeded())
                men.remove(i);
        }

        for (int j = 0; j < 2; j++)
            for (int i = 0; i < teamedMen[j].size(); i++) {
                if (!teamedMen[j].get(i).getIsNeeded())
                    teamedMen[j].remove(i);
            }

    }

    private void updateBullets(float dt) {
        for (Bullet b : bulls) {
            b.update(dt);
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
            for (ConvexPolygon bu : Barriers.getPotentialBarriers(b.getStart().getX(), b.getStart().getY())) {
                if (bu.containsBySegmentSide(b.getStart())) {
                    b.destroy();
                    drop(b, true);
                    break;
                }
            }

            for (ConvexPolygon bu : Barriers.getPotentialBarriers(b.getEnd().getX(), b.getEnd().getY())) {
                if (bu.containsBySegmentSide(b.getEnd())) {
                    b.destroy();
                    drop(b, true);
                    break;
                }
            }
        }
    }

    private void checkManAndBulletsForIntersection() {
        for (Bullet b : bulls) {
            if (b.getStart().getX() < 0 || b.getStart().getX() > displayWidth || b.getStart().getY() < 0 || b.getStart().getY() > displayHeight)
                b.destroy();
            else {
                int index = getNearestMenIndex(b.getStart().getX());
                int target = getNearestMenIndex(b.getEnd().getX());

                if (b.getDx() > 0) {
                    index++;
                    target = Math.min(target + 1, men.size() - 1);
                    for (int i = index; i < target; i++) {
                        if (men.get(i).getIsIntersect(b)) {
                            b.destroy();
                            drop(b, false);
                            break;
                        }
                    }
                } else {
                    for (int i = index; i >= target; i--) {
                        if (men.get(i).getIsIntersect(b)) {
                            b.destroy();
                            drop(b, false);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void removeUnusedBullets() {
        for (int i = 0; i < bulls.size(); i++) {
            if (!bulls.get(i).isNeedable()) {
                bulls.remove(i);
            }
        }
    }

    private void updateDrops(float dt) {
        for (Drops d : drops) {
            d.update(dt);
        }
    }

    private void removeUnusedDrops() {
        for (int i = 0; i < drops.size(); i++) {
            if (!drops.get(i).isNeedable()) {
                drops.remove(i);
            }
        }
    }

    private float angle = 0;

    private void spawnNewUnits() {
        if (rnd.nextInt(3) == 0) {
            shot(400, 240, angle);
            angle += 0.05f;
        }
    }

    private boolean getIsVisible(Man m1, Man m2) {
        return m1.getSquaredDistanceToPoint(m2) <= squaredVisibleDistance &&
                Barriers.getConnected(m1.getBlockX(), m1.getBlockY(), m2.getBlockX(), m2.getBlockY());
    }

    private void updateVisibility() {
        visibleMen[0].clear();
        visibleMen[1].clear();

        boolean[] visibility1=new boolean[teamedMen[0].size()];
        boolean[] visibility2=new boolean[teamedMen[1].size()];

        for (Man m1 : teamedMen[0]) m1.cleanVisibility();
        for (Man m2 : teamedMen[1]) m2.cleanVisibility();

        int num1=0;
        int num2;

        for (Man m1 : teamedMen[0]) {
            num2=0;
            for (Man m2 : teamedMen[1]) {
                if (!(visibility1[num1] && visibility2[num2]))
                    if (getIsVisible(m1, m2)) {

                        if (!visibility1[num1]) {
                            visibleMen[0].add(m1);
                            visibility1[num1]=true;
                            m1.addVisibleMan(m2);
                        }

                        if (!visibility2[num2]) {
                            visibleMen[1].add(m2);
                            visibility2[num2]=true;
                            m2.addVisibleMan(m1);
                        }

                        visibility1[num1]=true;
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
    }

    public void update(float dt) {
        updateMen(dt);
        removeUnusedMen();
        sortPeople();
        updateBullets(dt);
        checkManAndBulletsForIntersection();
        checkBuildingsAndBulletsForIntersection();
        removeUnusedBullets();
        updateDrops(dt);
        removeUnusedDrops();
        spawnNewUnits();
        updateAI(dt);
    }

    private void drawMenLayer() {
        for (Man m : teamedMen[0])
            m.draw(redID);
        for (Man m : teamedMen[1])
            m.draw(blueID);
    }

    private void drawDropsLayer() {
        /*Drops.startDraw();
        for (Drops m : drops)        TODO
            m.draw();*/
    }

    private void drawBulletsLayer() {
        /*Graphic.startDrawLines(0, 0, 0, 1, lineWidth);
        for (Bullet m : bulls) {
            m.draw();
        }                                    TODO
        Graphic.endDrawLines();*/
    }

    private void drawHousesLayer() {
        for (Building m : Barriers.getBarriers())
            m.draw(houseID);
    }

    private void draw() {
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
        drawMenLayer();
        drawDropsLayer();
        drawBulletsLayer();
        drawHousesLayer();
        end();
    }
}