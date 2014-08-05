package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Segment;

public class Tests {

    private static String get(int num) {
        String res=String.valueOf(num);
        while (res.length()<2)
            res+=" ";
        return res;
    }

    public static void main(String[] args) {
        Segment s[] =new Segment[4];
        s[0]=new Segment(306.8163f, 120.38625f, 286.91925f, 125.93325f);
        s[1]=new Segment(321.108f, 268.14435f, 300.21094f, 183.69133f);
        s[2]=new Segment(309.13947f, 219.52594f, 287.59263f, 133.68896f);
        s[3]=new Segment(318.75644f, 257.83737f, 297.20966f, 172.0004f);

        Man[] m=new Man[2];
        m[0]=new Man(304.8164f, 202.30382f, World.manSize, 0, 0);
        m[1]=new Man(321.108f, 268.14435f, World.manSize, 0, 0);

        for (int i=0; i<4; i++) {
            for (int j=0; j<2; j++)
                System.out.println("Man "+j+" Segment "+i+" : "+m[j].getIsIntersect(s[i]));
        }


    }

}
