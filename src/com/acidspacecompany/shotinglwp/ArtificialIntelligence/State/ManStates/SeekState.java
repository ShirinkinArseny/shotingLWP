package com.acidspacecompany.shotinglwp.ArtificialIntelligence.State.ManStates;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ManAI;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.Message;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ShittyAIModel.TackticAlgorithm;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.State.ManState;
import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

import java.util.List;
import java.util.Random;

public class SeekState extends ManState {
    /**
     * Этот класс --- синглтон
     */
    private SeekState() {}
    private static SeekState ourInstance = new SeekState();
    public static SeekState getInstance() {
        return ourInstance;
    }

    private static final Random rnd = new Random();

    @Override
    public void execute(float dt, ManAI man, List<Man> manList) {
        //Видны ли враги
        List<Man> enemies = man.getEnemies();
        //Если есть враги
        if (enemies!=null) {
            //Определяем сильнее мы или слабее
        }
        //Определяем движение по траектории
        man.executeAlgorithm();
    }

    @Override
    public void enterState(ManAI man, List<Man> manList) {
        /*//Задаем траекторию движения
        int count=manList.size();
        int width= World.getDisplayWidth();
        float lineWidth=(float)width/count;
        final float width_2=lineWidth/2;

        //Определяем, где в массиве расположен данный юнит
        int position = 0;
        for (Man man1 : manList) {
            if (man.equals(man1.getBrains()))
                break;
            else
                position++;
        }
        //Задаем синусоиду
        final float finalStartX = lineWidth*(0.5f + position);
        final float xAmp=rnd.nextFloat()*width_2*20+3;
        final float yAmp=World.getDisplayHeight()/World.manSpeed/2f;//даём некую свободу и временной запас
        man.setTacktickAlgo(new TackticAlgorithm() {
            boolean isOnTarget = false;

            @Override
            public void doSomAction(float absoluteTimeFromStart, Man m) {
                m.setTarget(new Point(
                        finalStartX +Math.sin(absoluteTimeFromStart/xAmp),
                        World.getDisplayHeight()/2*(1+Math.sin(absoluteTimeFromStart/(yAmp)))
                ));
            }
        });*/
    }

    @Override
    public void exitState(ManAI man, List<Man> manList) {

    }

    @Override
    public boolean messageDelivered(ManAI manAI, List<Man> manList, Message message) {
        return false;
    }
}
