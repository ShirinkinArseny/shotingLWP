package com.acidspacecompany.shotinglwp.ArtificialIntelligence.State;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ManAI;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.Message;

/**
 * Класс, определяющий поведение данного юнита
 */
public class ManState {
    private static ManState ourInstance = new ManState();

    public static ManState getInstance() {
        return ourInstance;
    }

    protected ManState(){}

    /**
     * Обновление данного состояния
     * @param dt изменение времени
     * @param man юнит, над которым проводятся операции
     */
    public void update(float dt, ManAI man) {

    }

    /**
     * Событие входа в данное состояние
     * Изменения состояний внутри данного метода запрещены!
     * @param man Доступ к человеку для операций
     */
    public void enterState(ManAI man) {

    }

    /**
     * Событие выхода из данного состояния
     * Изменение состояний внутри данного метода запрещены!
     * @param man Доступ к человеку для операций
     */
    public void exitState(ManAI man) {

    }

    /**
     * Событие прихода нового сообщения
     * @param message само сообщение
     * @return было ли сообщение обработано
     */
    public boolean messageDelivered(Message message) {
        return false;
    }
}
