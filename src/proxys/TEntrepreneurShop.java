/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.IEntrepreShop;
import message.Message;
import serverSide.ServerCom;


/**
 *
 * @author ribeiro
 */
public class TEntrepreneurShop extends Thread {

    private final ServerCom serverSocket;
    private final Message received;
    private final IEntrepreShop iShop;

    public TEntrepreneurShop(ServerCom serverSocket, Message received, IEntrepreShop iShop) {
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
            case "prepareToWork":
                iShop.prepareToWork();
                break;
            case "appraiseSit":
                iShop.appraiseSit();
                break;
            case "closeTheDoor":
                iShop.closeTheDoor();
                break;
            case "hasCustomersWaiting":
                sendValue = (Object) iShop.hasCustomersWaiting();
                break;
            case "addressACustomer":
                iShop.addressACustomer();
                break;
            case "sayGoodByeToCustomer":
                iShop.sayGoodByeToCustomer();
                break;
            case "prepareToLeave":
                iShop.prepareToLeave();
                break;
            case "returnToShop":
                iShop.returnToShop((int) received.getMessageContent());
                break;
            case "hasCallForMaterials":
                sendValue = (boolean) iShop.hasCallForMaterials();
                break;
            case "hasCallForProducts":
                sendValue = (boolean) iShop.hasCallForProducts();
                break;
            case "supplierHasMaterials":
                sendValue = (boolean) iShop.supplierHasMaterials();
                break;
            case "isShopClosed":
                sendValue = (boolean) iShop.isShopClosed();
                break;
            case "entrepreFinished":
                iShop.entrepreFinished();
        }

        sent = new Message("ack", received.getMessageID(), sendValue, "shop", 0, received.getSenderType(), received.getSenderID());

        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }
}
