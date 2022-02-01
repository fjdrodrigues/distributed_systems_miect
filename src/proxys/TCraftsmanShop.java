/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.ICraftsmanShop;
import message.Message;
import serverSide.ServerCom;


/**
 *
 * @author ribeiro
 */
public class TCraftsmanShop extends Thread {

    private final ServerCom serverSocket;
    private final Message received;
    private final ICraftsmanShop iShop;

    public TCraftsmanShop(ServerCom serverSocket, Message received, ICraftsmanShop iShop) {
        this.serverSocket = serverSocket;
        this.received = received;
        this.iShop = iShop;
    }

    @Override
    public void run() {

        Message sent;
        Object sendValue = null;

        switch (received.getMessageID()) {
            default:
                break;
            case "primeMaterialsNeeded":
                iShop.primeMaterialsNeeded(received.getSenderID());
                break;
            case "batchReadyForTransfer":
                iShop.batchReadyForTransfer(received.getSenderID());
                break;
        }

        sent = new Message("ack", received.getMessageID(), sendValue, "shop", 0, received.getSenderType(), received.getSenderID());

        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }

}
