package com.vdtt.network;

public interface IMessageHandler {

    public void onMessage(Message message);

    public void messageNotMap(Message ms);


    public void messageNotLogin(Message ms);

}