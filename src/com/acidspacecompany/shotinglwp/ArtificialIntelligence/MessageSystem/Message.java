package com.acidspacecompany.shotinglwp.ArtificialIntelligence.MessageSystem;

public class Message {
    //Доставка всем
    public static final int DELIVER_ALL = -1;
    //Немедленная отправка сообщения
    public static final float SEND_IMMEDIATELY = 0;


    //Время доставки
    private float deliverTime;
    public float getDeliverTime() {return deliverTime;}
    //ID адресата
    private int addresseeID;
    public int getAddresseeID() {return addresseeID;}

    //ID отправителя
    private int senderID;
    public int getSenderID() {return senderID;}

    //Сообщение (Один из ID, хранящихся в классе TypicalMessages)
    private int message;

    public Message(int senderID, int addresseeID, float deliverTime, int message) {
        this.senderID = senderID;
        this.addresseeID = addresseeID;
        this.deliverTime = deliverTime;
        this.message = message;
    }

    /**
     * Приближение времени доставки
     * @param dt Время, на которое доставка приблизилась
     */
    public void decreaseTime(float dt) {
        deliverTime -= dt;
    }

    /**
     * Определение того, нужно ли доставлять сообщение
     * @return необходимость доставки
     */
    public boolean isNeedToDeliver() {
        return deliverTime <= 0;
    }

}
