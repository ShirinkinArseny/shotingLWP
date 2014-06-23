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

    public enum Connection {  //Степень просматриваемости квадратов
        connected,            //Полностью просматриваются
        disconnected,         //Полностью закрыты
        partiallyConnected
    }   //Частичная видимость

    public enum Intersection { //Степень пересечения отрезка
        full,                  //Занят полностью
        none,                  //Свободне полностью
        partial                //Частично занят, частично свободен
    }

    public static ArrayList<Building> getEmptyList() {
        return emptyList;
    }

    private static ArrayList<Building> barriers = new ArrayList<>();
    private static float blockWidth;
    private static float blockHeight;
    private static final int horizontalCount = 20;
    private static final int verticalCount = 10;

    private static ArrayList[][] barriersInCells;
    private static Connection[][][][] connections;

    private static final ArrayList<Building> emptyList = new ArrayList<>();

    public static int getXBlock(float x) {
        return (int) (x / blockWidth);
    }

    public static int getYBlock(float y) {
        return (int) (y / blockHeight);
    }

    public static ArrayList<Building> getPotentialBarriers(int xBlock, int yBlock) {
        if (xBlock >= horizontalCount || xBlock < 0) return emptyList;
        if (yBlock >= verticalCount || yBlock < 0) return emptyList;
        return barriersInCells[xBlock][yBlock];
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

    private static float getProjectedX(float x, float y, float sin, float cos) {
        return x * cos - y * sin;
    }

    private static float getProjectedX(Point p, float sin, float cos) {
        return getProjectedX(p.getX(), p.getY(), sin, cos);
    }

    //Вычитает из отрезков decrimented отрезок decrimentor
    private static void decriment(LinkedList<float[]> decrimented, float[] decrimentor) {
        for (int i = 0; i < decrimented.size(); i++) {

            if (decrimented.get(i)[1] < decrimentor[0] || decrimented.get(i)[0] > decrimentor[1])
                continue;

            if (decrimented.get(i)[0] >= decrimentor[0] && decrimented.get(i)[1] <= decrimentor[1])
                decrimented.remove(i);

            else if (decrimented.get(i)[0] > decrimentor[0] && decrimented.get(i)[1] >= decrimentor[1])
                decrimented.get(i)[0] = decrimentor[1];

            else if (decrimented.get(i)[0] <= decrimentor[0] && decrimented.get(i)[1] < decrimentor[1])
                decrimented.get(i)[1] = decrimentor[0];

            else if (decrimented.get(i)[0] <= decrimentor[0] && decrimented.get(i)[1] >= decrimentor[1]) {
                decrimented.get(i)[1] = decrimentor[0];
                decrimented.add(new float[]{decrimentor[1], decrimented.get(i)[1]});
            }

        }
    }

    //Возвращает пересечение отрезков points c отрезком neededPoints
    private static Intersection getIntersection(LinkedList<float[]> points, float[] neededPoints) {
        LinkedList<float[]> segm = new LinkedList<>();
        segm.add(new float[]{neededPoints[0], neededPoints[1]});//to not link them
        for (float[] p : points)
            decriment(segm, p);
        if (segm.isEmpty())
            return Intersection.full;
        if (segm.size() == 1) {
            if (segm.get(0)[0] == neededPoints[0] && segm.get(0)[1] == neededPoints[1])
                return Intersection.none;
        }
        return Intersection.partial;
    }

    //Получает один спроецированный отрезок из списка проекций вершин замкнутой фигуры
    private static float[] getSegment(float[] vertexes) {
        int min = 0;
        int max = 0;
        for (int i = 1; i < vertexes.length; i++) {
            if (vertexes[i] < vertexes[min]) min = i;
            else if (vertexes[i] > vertexes[max]) max = i;
        }
        return new float[]{vertexes[min], vertexes[max]};
    }

    //Получает проекцию фигуры
    private static float[] getSegment(ConvexPolygon b, Segment checker, float sin, float cos) {
        LinkedList<Float> segms = new LinkedList<>();
        for (Point s : b.getPoints())
            if (checker.getIsBetween(s))
                segms.add(getProjectedX(s, sin, cos));
        if (segms.isEmpty()) return null;
        float[] verts = new float[segms.size()];
        for (int i = 0; i < segms.size(); i++)
            verts[i] = segms.get(i);
        return getSegment(verts);
    }

    //Возвращает соединённость двух квадратов
    public static Connection getConnected(int x0, int y0, int x1, int y1) {
        if (x0 >= horizontalCount || x0 < 0) return Connection.disconnected;
        if (y0 >= verticalCount || y0 < 0) return Connection.disconnected;
        if (x1 >= horizontalCount || x1 < 0) return Connection.disconnected;
        if (y1 >= verticalCount || y1 < 0) return Connection.disconnected;
        return connections[x0][y0][x1][y1];
    }

    //ПОлучает соединённость двух квадратов
    private static Connection getSlowConnected(int x0, int y0, int x1, int y1) {
        /* Принцип работы:
           Представляем систему координат, в которой прямая, соединяющая центры проверяемых квадратов - ось Oy.
           Осуществляем перенос точек из одной системы координат в другую.
           При этом важны только новые относительные значения точек на оси Ox.
           Точки на новой оси Ox - это проекции точек.
           Отрезки, образуемые точками - проекции отрезков.
           Если проекции, образуемые препятствиями, находящимися между точками,
           (между - значит оба угла от точки препятствия к отрезку между квадратами острые)
           полностью заполняют проекцию, образованную самим квадратом - то видимость между квадратами отсутствует.
           Если лишь частично заполняет - видимость частична.
           Если проекция квадрата не пересекается ни с одной другой проекцией - видимость полная.
        */
        float alpha = (float) Math.atan2(y1 - y0, x1 - x0);
        float cos = (float) Math.sin(alpha);
        float sin = (float) Math.cos(alpha);
        Segment segment = new Segment((x0 + 0.5f) * blockWidth, (y0 + 0.5f) * blockHeight,
                (x1 + 0.5f) * blockWidth, (y1 + 0.5f) * blockHeight);
        LinkedList<float[]> segments = new LinkedList<>();
        for (Building b : barriers) {
            float[] f = getSegment(b, segment, sin, cos);
            if (f != null)
                segments.add(f);
        }
        if (segments.isEmpty()) return Connection.connected;
        float[] checkedSegment = getSegment(new float[]{
                getProjectedX((x1) * blockWidth, (y1) * blockHeight, sin, cos),
                getProjectedX((x1 + 1) * blockWidth, (y1) * blockHeight, sin, cos),
                getProjectedX((x1) * blockWidth, (y1 + 1) * blockHeight, sin, cos),
                getProjectedX((x1 + 1) * blockWidth, (y1 + 1) * blockHeight, sin, cos)
        });
        switch (getIntersection(segments, checkedSegment)) {
            case full:
                return Connection.disconnected;
            case none:
                return Connection.connected;
            case partial:
                return Connection.partiallyConnected;
        }

        return null;
    }

    public static void finishAddingBarriers() {
        Collections.sort(barriers, new Comparator<Building>() {
            @Override
            public int compare(Building building, Building building2) {
                return Float.compare(building.getCentre().getX(), building2.getCentre().getX());
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
                    if (b.intersect(cell))
                        barriersInCells[i][j].add(b);
                }
            }

        connections=new Connection[horizontalCount][verticalCount][horizontalCount][verticalCount];
        for (int i = 0; i < horizontalCount; i++) {
            for (int j = 0; j < verticalCount; j++) {
                for (int k = 0; k < horizontalCount; k++) {
                    for (int l = 0; l < verticalCount; l++) {
                        connections[i][j][k][l]=getSlowConnected(i, j, k, l);
                    }
                }
                Log.i("MAP", i+":"+j+" calculated");
            }
        }
    }

    public static void printSOPMap() {
        String res = "Outputting map:\n";
        for (int i = 0; i < verticalCount; i++) {
            res += '|';
            for (int j = 0; j < horizontalCount; j++) {
                res += barriersInCells[j][i].isEmpty() ? ' ' : 'o';
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
                res += barriersInCells[j][i].isEmpty() ? ' ' : 'o';
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
        blockWidth = w / horizontalCount;
        blockHeight = h / verticalCount;
        barriersInCells = new ArrayList[horizontalCount][verticalCount];
        for (int i = 0; i < horizontalCount; i++)
            for (int j = 0; j < verticalCount; j++)
                barriersInCells[i][j] = new ArrayList<>();
        //connections=new Connection[horizontalCount][verticalCount][horizontalCount][verticalCount];
    }


}
