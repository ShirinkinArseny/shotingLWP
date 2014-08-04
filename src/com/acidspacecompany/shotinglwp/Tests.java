package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Segment;

public class Tests {

    public static void main(String[] args) {
        System.out.println(new Man(0, 0, 20, 0, 0).getIsIntersect(new Segment(-20, 30, 20, 0)));
    }

}
