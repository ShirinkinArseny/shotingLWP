package com.acidspacecompany.shotinglwp.ArtificialIntelligence;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.Message;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.MessageHandler;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ShittyAIModel.TackticAlgorithm;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.State.ManState;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.State.ManStates.SeekState;
import com.acidspacecompany.shotinglwp.GameObjects.Man;
import com.acidspacecompany.shotinglwp.Geometry.Point;

import java.util.List;

/**
 * Класс, в котором будет выполняться AI для каждого юнита
 */
public class ManAI {
    //Тело, которым мы будем оперировать
    private Man body;
    public Point getPosition() {
        return body;
    }
    public Point getSpeed() {return body.getSpeed();}
    public List<Man> getEnemies() {return body.getVisibleMan(); }
    //Текущее состояние юнита
    private ManState currentState;
    //Предыдущее состояние юнита
    private ManState previousState;
    //Глобальное состояние юнита
    private ManState globalState;
    //Команда
    private List<Man> team;

    private float age=0;

    private TackticAlgorithm algorithm;
    public void setTacktickAlgo(TackticAlgorithm ta) {
        algorithm=ta;
    }
    public void executeAlgorithm() {
        algorithm.doSomAction(age, body);
    }

    //Система для сообщения между юнитами
    private MessageHandler messageHandler;
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    public void sendMessage(int addresseeID, float deliverTime, int message) {
        messageHandler.addMessage(new Message(unitID, addresseeID, deliverTime, message));
    }
    public void sendMessageToMe(float deliverTime, int message) {
        messageHandler.addMessage(new Message(unitID, unitID, deliverTime, message));
    }



    //ID юнита (для сообщений)
    int unitID;
    public int getUnitID() {return unitID;}
    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }


    public ManAI(Man body) {
        this.body = body;
        currentState = SeekState.getInstance();
        previousState = SeekState.getInstance();
        globalState = SeekState.getInstance();
    }
    public void init(MessageHandler messageHandler, int unitID, List<Man> team) {
        this.messageHandler = messageHandler;
        this.unitID = unitID;
        this.team = team;
        setState(SeekState.getInstance());
    }

    public void update(float dt) {
        //Обновляем состояние юнита
        currentState.execute(dt, this,team);
        age+=dt;
    }

    public void setState(ManState newState) {
        //Присваиваем предыдущее состояние
        previousState = currentState;
        //Выходим из предыдущего состояния
        currentState.exitState(this,team);
        currentState = newState;
        //Входим в новое
        currentState.enterState(this,team);
    }
    public void returnToPreviousState() {
        setState(previousState);
    }
    public void returnToGlobalState() {
        setState(globalState);
    }

    /**
     * Событие прихода нового сообщения
     * @param message сообщение
     */
    public void messageDelivered(Message message) {
        //Если текущее состояние не обработало сообщение
        //Например, если это сообщение предназначено не для этого состояния
        if (!currentState.messageDelivered(this, team, message))
            //То, скорее всего, оно предназначено для глобального состояния
            globalState.messageDelivered(this, team, message);
    }

}
