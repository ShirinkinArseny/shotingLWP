package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.ConvexPolygon;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;

import java.util.LinkedList;
import java.util.Random;

public class SpeedTesting {

    private static long run(Runnable r) {
        long l=System.currentTimeMillis();
        r.run();
        return System.currentTimeMillis()-l;
    }

    private static void compare(Runnable r1, Runnable r2) {
        long l1=run(r1);
        long l2=run(r2);
        System.out.println((l1<l2?"First":"Second")+" method is faster");
        System.out.println("Fisrt  result: "+l1+" miliseconds");
        System.out.println("Second result: "+l2+" miliseconds");
    }

    public static void main(String[] args) {
        Random rnd=new Random();

        final LinkedList<Segment> lines=new LinkedList<>();
        for (int i=0; i<1000; i++) {
            int x=rnd.nextInt(800);
            int y=rnd.nextInt(800);

            lines.add(new Segment(x, y, x+rnd.nextInt(20), y+rnd.nextInt(20)));
        }


        final LinkedList<ConvexPolygon> polys=new LinkedList<>();
        for (int i=0; i<1000; i++) {
            int x=rnd.nextInt(800);
            int y=rnd.nextInt(800);

            polys.add(new ConvexPolygon(new Point[] {
                    new Point(x-100, y),
                    new Point(x, y-100),
                    new Point(x+100, y),
                    new Point(x, y+100),
            }));
        }

        for (int i=0; i<1000; i++) {
            int x=rnd.nextInt(800);
            int y=rnd.nextInt(800);

            polys.add(new ConvexPolygon(new Point[] {
                    new Point(x-100, y),
                    new Point(x-70, y-70),
                    new Point(x+100, y),
                    new Point(x+70, y+70),
                    new Point(x, y+100),
                    new Point(x-70, y+70),
                    new Point(x-100, y),
                    new Point(x-70, y-70),
            }));
        }


        Runnable r1=new Runnable() {
            @Override
            public void run() {
                for (Segment s: lines)
                    for (ConvexPolygon c: polys)
                        c.intersectsByPointContainment(s);
            }
        };

        Runnable r2=new Runnable() {
            @Override
            public void run() {
                for (Segment s: lines)
                    for (ConvexPolygon c: polys)
                        c.intersectsBySegmentIntersection(s);
            }
        };

        compare(r1, r2);
    }

}
