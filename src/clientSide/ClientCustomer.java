/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import interfaces.ICustomer;
import java.io.IOException;
import message.Message;

/**
 *
 * @author ribeiro
 */
public class ClientCustomer implements ICustomer {

    private ClientCom comClient;

    public ClientCustomer(String ipAddress, int portNumber) {
        comClient = new ClientCom(ipAddress, portNumber);
    }

    @Override
    public synchronized void goShopping(int custID) {
        sendMessage("goShopping", null, custID);
    }

    @Override
    public synchronized boolean isDoorOpen() {
        return (boolean) sendMessage("isDoorOpen", null, -1);

    }

    @Override
    public synchronized void tryAgainLater(int custID) {
        sendMessage("tryAgainLater", null, custID);
    }

    @Override
    public synchronized void enterShop(int custID) {
        sendMessage("enterShop", null, custID);
    }

    @Override
    public synchronized int perusingAround() {
        return (int) sendMessage("perusingAround", null, -1);
    }

    @Override
    public synchronized void iWantThis(int custID, int nProducts) {
        sendMessage("iWantThis", (Object) nProducts, custID);
    }

    @Override
    public synchronized void exitShop(int custID) {
        sendMessage("exitShop", null, custID);
    }

    @Override
    public synchronized boolean shopHasProducts() {
        return (boolean) sendMessage("shopHasProducts", null, -1);
    }

    @Override
    public synchronized boolean isShopClosed() {
        return (boolean) sendMessage("isShopClosed", null, -1);
    }

    @Override
    public synchronized void custFinished(int custID) {
        sendMessage("custFinished", null, custID);
    }

    private synchronized Object sendMessage(String action, Object content, int requesterID) {
        Object returnVal;
        Message msg = new Message("notification", action, content, "customer", requesterID, "shop", 0);
        while (!comClient.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }        //System.out.println("SENDING:\n" + msg.toString());
        comClient.writeObject(msg);
        Message reply = (Message) comClient.readObject();

        while (!(reply.getMessageType().equals("ack") && reply.getSenderType().equals("shop")
                && reply.getReceiverType().equals("customer") && reply.getReceiverID() == requesterID
                && reply.getMessageID().equals(action))) {
            reply = (Message) comClient.readObject();
        }
        //System.out.println("RECEIVING:\n" + reply.toString());

        returnVal = (Object) reply.getMessageContent();
        comClient.close();
        return returnVal;
    }

}
