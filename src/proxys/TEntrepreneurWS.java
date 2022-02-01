/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.IEntrepreWorkShop;
import message.Message;
import serverSide.ServerCom;


/**
 *
 * @author ribeiro
 */
public class TEntrepreneurWS extends Thread{
    
    private final ServerCom serverSocket;
    private final Message received;
    private final IEntrepreWorkShop iWorkShop;
    
    public TEntrepreneurWS(ServerCom serverSocket, Message received, IEntrepreWorkShop iWorkShop)
    {
        this.serverSocket = serverSocket;
        this.received = received;
        this.iWorkShop = iWorkShop;
    }

    @Override
    public void run() {
        
        Message sent;
        Object sendValue = null;
        
        switch(received.getMessageID())
        {
            default:
                break;
            case "goToWorkShop":
                sendValue = (Object) iWorkShop.goToWorkShop();
                break;
            case "visitSuppliers":
                sendValue = (Object) iWorkShop.visitSuppliers();
                break;
            case "replenishStock":
                iWorkShop.replenishStock((int) received.getMessageContent());
                break;
        }
        
        sent = new Message("ack", received.getMessageID(), sendValue, "workshop", 0, received.getSenderType(),
                received.getSenderID());
        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }
    
}
