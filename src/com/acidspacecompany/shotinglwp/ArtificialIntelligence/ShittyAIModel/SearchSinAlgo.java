package com.acidspacecompany.shotinglwp.ArtificialIntelligence.ShittyAIModel;

import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

import java.util.List;
import java.util.Random;

public class SearchSinAlgo {

    public static void setSearchAlgo(List<Man> men) {

        int count=men.size();
        int width= World.getDisplayWidth();
        float lineWidth=(float)width/count;
        final Random r=new Random();
        final float width_2=lineWidth/2;


        float startX=lineWidth/2;
        for (Man m: men) {
            final float finalStartX = startX;
            final float xAmp=r.nextFloat()*width_2*20+3;
            final float yAmp=World.getDisplayHeight()/World.manSpeed/2f;//даём некую свободу и временной запас
            final float dy=r.nextFloat()*3.14f;//даём некую свободу и временной запас
            final float dx=(r.nextFloat()+0.3f)*width_2;
            m.getBrains().setTacktickAlgo(new TackticAlgorithm() {
                @Override
                public void doSomAction(float absoluteTimeFromStart, Man m) {
                     m.setTarget(new Point(
                             finalStartX +Math.sin(absoluteTimeFromStart/xAmp)*dx,
                             World.getDisplayHeight()/2*(1+Math.sin(dy+absoluteTimeFromStart/(yAmp)))
                             ));
                }
            });
            startX+=lineWidth;
        }

    }

}
