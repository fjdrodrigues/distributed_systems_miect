/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.ICraftsmanWorkShop;
import message.Message;
import serverSide.ServerCom;

/**
 *
 * @author João
 */
public class TCraftsmanWS extends Thread{
    
    private final ServerCom serverSocket;
    private final Message received;
    private final ICraftsmanWorkShop iWorkShop;
    
    public TCraftsmanWS(ServerCom serverSocket, Message received, ICraftsmanWorkShop iWorkShop) {
        this.serverSocket = serverSocket;
        this.received = received;
        this.iWorkShop = iWorkShop;
    }
    
    @Override
    public void run() {

        Message sent;
        Object sendValue = null;

        switch (received.getMessageID()) {
            default:
                break;
            case "checkForMaterials":
                sendValue = (Object) iWorkShop.checkForMaterials();
                break;
            case "lowMaterialsInWorkshop":
                sendValue = (Object) iWorkShop.lowMaterialsInWorkshop();
                break;
            case "collectMaterials":
                iWorkShop.collectMaterials(received.getSenderID());
                break;
            case "prepareToProduce":
                iWorkShop.prepareToProduce(received.getSenderID());
                break;
            case "goToStore":
                iWorkShop.goToStore(received.getSenderID());
                break;
            case "backToWork":
                iWorkShop.backToWork(received.getSenderID());
                break;
            case "materialsNotDepleted":
                sendValue = (Object) iWorkShop.materialsNotDepleted();
                break;
            case "isEntrepreCalledMaterials":
                sendValue = (Object) iWorkShop.isEntrepreCalledMaterials();
                break;
            case "isEntrepreCalledProducts":
                sendValue = (Object) iWorkShop.isEntrepreCalledProducts();
                break;
            case "isBatchReady":
                sendValue = (Object) iWorkShop.isBatchReady();
                break;
            case "craftsFinished":
                iWorkShop.craftsFinished(received.getSenderID());
                break;
        }
        sent = new Message("ack", received.getMessageID(), sendValue, "workshop", 0, received.getSenderType(),
                received.getSenderID());
        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }
}
