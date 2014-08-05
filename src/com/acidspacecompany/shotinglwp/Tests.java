package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.Geometry.Segment;

public class Tests {

    private static String get(int num) {
        String res=String.valueOf(num);
        while (res.length()<2)
            res+=" ";
        return res;
    }

    public static void main(String[] args) {
        int size=10;

        Segment s=new Segment(0, 3, 3, 3.3f);
        int[][] m=new int[size][size];
        for (int i=0; i<size; i++)
            for (int j=0; j<size; j++) {
                m[i][j]= (int) s.getLengthToLine(new Point(i, j));
            }

        for (int i=0; i<size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(get(m[j][i]));
            }
            System.out.println();
        }
    }

}
