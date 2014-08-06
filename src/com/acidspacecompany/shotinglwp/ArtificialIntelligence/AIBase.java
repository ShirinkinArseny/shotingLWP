package com.acidspacecompany.shotinglwp.ArtificialIntelligence;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.MessageHandler;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ShittyAIModel.SearchSinAlgo;
import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AIBase {

    private List<Man> ourTeam,visibleEnemies;

    private CommandoAI commandoAI;

    private MessageHandler messageHandler;

    private int currentID;

    public AIBase(List<Man> ourTeam, List<Man> visibleEnemies) {
        this.ourTeam = ourTeam;
        this.visibleEnemies = visibleEnemies;
        messageHandler = new MessageHandler(ourTeam);
        ManAI manAI;
        currentID = 0;
        for (Man unit : ourTeam) {
            manAdded(unit);
        }
        commandoAI = new CommandoAI(ourTeam, visibleEnemies);
    }

    public void manAdded(Man addedMan) {
        addedMan.getBrains().init(messageHandler, currentID, ourTeam);
        commandoAI.unitAdded(addedMan);
        currentID++;
    }

    public void update(float dt) {
        messageHandler.update(dt);
        for (Man unit : ourTeam) {
            unit.getBrains().update(dt);
        }
    }
}


