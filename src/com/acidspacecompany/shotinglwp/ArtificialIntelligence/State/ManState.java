package com.acidspacecompany.shotinglwp.ArtificialIntelligence.State;

import com.acidspacecompany.shotinglwp.ArtificialIntelligence.ManAI;
import com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem.Message;
import com.acidspacecompany.shotinglwp.GameObjects.Man;

import java.util.List;

/**
 * Класс, определяющий поведение данного юнита
 */
public abstract class ManState {
    /**
     * Выполнение данного состояния
     * @param dt изменение времени
     * @param man юнит, над которым проводятся операции
     */
    public abstract void execute(float dt, ManAI man, List<Man> manList);

    /**
     * Событие входа в данное состояние
     * Изменения состояний внутри данного метода запрещены!
     * @param man Доступ к человеку для операций
     */
    public abstract void enterState(ManAI man, List<Man> manList);

    /**
     * Событие выхода из данного состояния
     * Изменение состояний внутри данного метода запрещены!
     * @param man Доступ к человеку для операций
     */
    public abstract void exitState(ManAI man, List<Man> manList);

    /**
     * Событие прихода нового сообщения
     * @param message само сообщение
     * @return было ли сообщение обработано
     */
    public abstract boolean messageDelivered(ManAI manAI, List<Man> manList, Message message);
}
