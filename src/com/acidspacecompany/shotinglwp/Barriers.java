package com.acidspacecompany.shotinglwp;

import android.util.Log;
import com.acidspacecompany.shotinglwp.Geometry.ConvexPolygon;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Barriers {

    private static ArrayList<Building> barriers = new ArrayList<>();
    private static final int blockWidth=5;
    private static final int blockHeight=5;
    private static int horizontalCount;
    private static int verticalCount;

    private static ArrayList<Building>[][] barriersInCells;
    private static boolean[][] barriersInCellsContains;

    private static final ArrayList<Building> emptyList = new ArrayList<>();

    public static int getXBlock(float x) {
        return (int) (x / blockWidth);
    }

    public static int getYBlock(float y) {
        return (int) (y / blockHeight);
    }

    public static boolean getContainsPotentialBarriers(int xBlock, int yBlock) {
        return !(xBlock >= horizontalCount || xBlock < 0 || yBlock >= verticalCount || yBlock < 0)
                && barriersInCellsContains[xBlock][yBlock];
    }

    public static ArrayList<Building> getPotentialBarriers(float x, float y) {
        int xBlock = getXBlock(x);
        int yBlock = getYBlock(y);
        if (xBlock >= horizontalCount || xBlock < 0) return emptyList;
        if (yBlock >= verticalCount || yBlock < 0) return emptyList;
        return barriersInCells[xBlock][yBlock];
    }

    public static float getBlockWidth() {
        return blockWidth;
    }

    public static float getBlockHeight() {
        return blockHeight;
    }

    //Возвращает соединённость двух квадратов
    public static boolean getConnected(int x0, int y0, int x1, int y1) {
        Segment s=new Segment(x0, y0, x1, y1);
        if (x0 >= horizontalCount || x0 < 0) return false;
        if (y0 >= verticalCount || y0 < 0) return false;
        if (x1 >= horizontalCount || x1 < 0) return false;
        if (y1 >= verticalCount || y1 < 0) return false;


        float l=s.getLength();
        float x=x0;
        float y=y0;
        float dx=(x1-x0)/l;
        float dy=(y1-y0)/l;
        for (int i=0; i<l; i++) {
            if (barriersInCellsContains[((int) x)][((int) y)])
                return false;
            x+=dx;
            y+=dy;
        }

        return true;
    }

    public static void finishAddingBarriers() {
        Collections.sort(barriers, new Comparator<ConvexPolygon>() {
            @Override
            public int compare(ConvexPolygon building, ConvexPolygon building2) {
                return Float.compare(building.getX(), building2.getX());
            }
        });

        for (int i = 0; i < horizontalCount; i++)
            for (int j = 0; j < verticalCount; j++) {
                ConvexPolygon cell = new ConvexPolygon(new Point[]{
                        new Point(i * blockWidth, j * blockHeight),
                        new Point((i + 1) * blockWidth, j * blockHeight),
                        new Point((i + 1) * blockWidth, (j + 1) * blockHeight),
                        new Point(i * blockWidth, (j + 1) * blockHeight),
                });
                for (Building b : barriers) {
                    if (b.intersectBySegmentsIntersection(cell)) {
                        barriersInCells[i][j].add(b);
                    barriersInCellsContains[i][j]=true;        }
                }
            }
    }

    public static void printSOPMap() {
        String res = "Outputting map:\n";
        for (int i = 0; i < verticalCount; i++) {
            res += '|';
            for (int j = 0; j < horizontalCount; j++) {
                res += barriersInCellsContains[j][i] ? 'o' : ' ';
            }
            res += "|\n";
        }
        System.out.println(res);
    }

    public static void printLogIMap() {
        String res = "Outputting map:\n";
        for (int i = 0; i < verticalCount; i++) {
            res += '|';
            for (int j = 0; j < horizontalCount; j++) {
                res += barriersInCellsContains[j][i] ? 'o' : ' ';
            }
            res += "|\n";
        }
        Log.i("Map", res);
    }

    public static void addBarrier(Building b) {
        barriers.add(b);
    }

    public static ArrayList<Building> getBarriers() {
        return barriers;
    }

    public static void init(float w, float h) {
        horizontalCount = (int) (w / blockWidth)+1;
        verticalCount = (int) (h / blockHeight)+1;
        barriersInCells = new ArrayList[horizontalCount][verticalCount];
        barriersInCellsContains=new boolean[horizontalCount][verticalCount];
        for (int i = 0; i < horizontalCount; i++)
            for (int j = 0; j < verticalCount; j++) {
                barriersInCells[i][j] = new ArrayList<>();
                barriersInCellsContains[i][j]=false;
            }
    }


}
