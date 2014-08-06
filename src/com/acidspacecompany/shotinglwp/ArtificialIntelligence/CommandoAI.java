package com.acidspacecompany.shotinglwp.ArtificialIntelligence;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ShittyAIModel.SearchSinAlgo;
import com.acidspacecompany.shotinglwp.GameObjects.Man;

import java.util.List;

public class CommandoAI {
    List<Man> ourUnits, enemyUnits;

    public CommandoAI(List<Man> ourUnits, List<Man> enemyUnits) {
        this.ourUnits = ourUnits;
        this.enemyUnits = enemyUnits;
    }

    public void unitAdded(Man unit) {
        SearchSinAlgo.setSearchAlgo(ourUnits);
    }
}
