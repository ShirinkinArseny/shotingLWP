package com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem;

import com.acidspacecompany.shotinglwp.GameObjects.Man;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для отправки сообщений внутри команды.
 * Сообщения можно отправлять как самому себе, так и всей команде в целом.
 * Сообщения могут быть как отложенными, так и немедленными
 */
public class MessageHandler {
    //Сообщения к доставке
    private LinkedList<Message> messages = new LinkedList<>();
    public void addMessage(Message message) {
        messages.add(message);
    }

    //Список всех людей в команде (для доставки им сообщения)
    private List<Man> teamUnits;
    public MessageHandler(List<Man> teamUnits) {
        this.teamUnits = teamUnits;
    }


    public void update(float dt) {
        Iterator<Message> iterator = messages.iterator();
        Message message;
        while (iterator.hasNext()){
            message = iterator.next();
            message.decreaseTime(dt);
            //Доставляем сообщение
            if (message.isNeedToDeliver()) {
                //Получаем адресата
                int addresseeID = message.getAddresseeID();

                //Выполняем отправку
                //Если доставляем всем, то отправляем всем сразу
                if (addresseeID == Message.DELIVER_ALL) {
                    for (Man unit : teamUnits) {
                        unit.getBrains().messageDelivered(message);
                    }
                }
                //Иначе ищем адресата
                else {
                    for (Man unit : teamUnits) {
                        if (unit.getBrains().getUnitID() == addresseeID) {
                            //Доставляем сообщение
                            unit.getBrains().messageDelivered(message);
                            //Выходим из цикла
                            return;
                        }
                    }
                }

                //Убираем из списка доставленное сообщение
                iterator.remove();
            }
        }
    }
}
