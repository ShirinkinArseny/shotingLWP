package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.GameObjects.Building;
import com.acidspacecompany.shotinglwp.GameObjects.Buildings;

public class Tests {

    public static void main(String[] args) {
        Buildings.init(800, 480);
        Buildings.addBarrier(new Building(240,
                80, 60, 30));
        Buildings.addBarrier(new Building(240,
                200, 60, 30));
        Buildings.addBarrier(new Building(240,
                320, 60, 30));
        Buildings.addBarrier(new Building(560,
                80, 60, 30));
        Buildings.addBarrier(new Building(560,
                200, 60, 30));
        Buildings.addBarrier(new Building(560,
                320, 60, 30));
        Buildings.finishAddingBarriers();

        System.out.println(Buildings.getConnected(180, 40, 180, 120));
    }

}
