package com.acidspacecompany.shotinglwp.ArtificialIntelligence;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.MessageHandler;
import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AIBase {

    private List<Man> ourTeam,visibleEnemies;

    private MessageHandler messageHandler;

    private int currentID;

    public AIBase(List<Man> ourTeam, List<Man> visibleEnemies) {
        this.ourTeam = ourTeam;
        this.visibleEnemies = visibleEnemies;
        messageHandler = new MessageHandler(ourTeam);
        ManAI manAI;
        currentID = 0;
        for (Man unit : ourTeam) {
            manAI = unit.getBrains();
            manAI.setMessageHandler(messageHandler);
            manAI.setUnitID(currentID);
            currentID++;
        }
    }

    public void manAdded(Man addedMan) {
        ManAI ai = addedMan.getBrains();
        ai.setMessageHandler(messageHandler);
        ai.setUnitID(currentID);
        currentID++;
    }

    public void update(float dt) {
        messageHandler.update(dt);
        for (Man unit : ourTeam) {
            unit.getBrains().update(dt);
        }
    }
}


