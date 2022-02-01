/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.ICustomer;
import message.Message;
import serverSide.ServerCom;


/**
 *
 * @author ribeiro
 */
public class TCustomerShop extends Thread {

    private final ServerCom serverSocket;
    private final Message received;
    private final ICustomer iShop;

    public TCustomerShop(ServerCom serverSocket, Message received, ICustomer iShop) {
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
            case "goShopping":
                iShop.goShopping(received.getSenderID());
                break;
            case "isDoorOpen":
                sendValue = (Object) iShop.isDoorOpen();
                break;
            case "tryAgainLater":
                iShop.tryAgainLater(received.getSenderID());
                break;
            case "enterShop":
                iShop.enterShop(received.getSenderID());
                break;
            case "perusingAround":
                sendValue = (Object) iShop.perusingAround();
                break;
            case "iWantThis":
                iShop.iWantThis(received.getSenderID(), (int) received.getMessageContent());            //MUDAR O 10
                break;
            case "exitShop":
                iShop.exitShop(received.getSenderID());
                break;
            case "shopHasProducts":
                sendValue = (boolean) iShop.shopHasProducts();
                break;
            case "isShopClosed":
                sendValue = (boolean) iShop.isShopClosed();
                break;
            case "custFinished":
                iShop.custFinished(received.getSenderID());
                break;
        }
        sent = new Message("ack", received.getMessageID(), sendValue, "shop", 0, received.getSenderType(), received.getSenderID());

        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }

}
