package com.acidspacecompany.shotinglwp.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    private ArrayList<ServerConnection> clients;
    private Runnable event;
    private String lastMessage=null;

    public String getLastInputMessage() {
        return lastMessage;
    }

    public void sendMessage(String s) {
       for (ServerConnection c: clients)
           c.sendMessage(s);
    }

    public void setOnInputEvent(Runnable r) {
        event=r;
    }

    public Server(final int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                clients=new ArrayList<>();
                ServerSocket server;
                try {
                    server = new ServerSocket(port);
                    while (true) {
                        final Socket client = server.accept();
                        final ServerConnection cl = new ServerConnection(client);
                        clients.add(cl);
                        cl.setOnInputEvent(new Runnable() {
                            @Override
                            public void run() {    lastMessage=cl.getLastInputMessage();
                                event.run();
                                System.out.println("[Server] Incoming message: " + lastMessage);

                            }
                        });
                        cl.setOnCloseEvent(new Runnable(){
                            @Override
                            public void run() {
                                System.out.println("[Server] Disconnected client: " + client.getInetAddress().toString());
                                clients.remove(cl);
                            }
                        });
                        System.out.println("[Server] Got client: "+client.getInetAddress());
                    }
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        new Server(1234);
    }

}