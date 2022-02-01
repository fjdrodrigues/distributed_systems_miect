/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import interfaces.IShop;
import message.Message;
import serverSide.ServerCom;

/**
 *
 * @author ribeiro
 */
public class TShopRepo extends Thread {

    private final IShop iRepo;
    private final ServerCom serverSocket;
    private final Message received;

    public TShopRepo(ServerCom serverSocket, Message received, IShop iRepo) {
        this.serverSocket = serverSocket;
        this.received = received;
        this.iRepo = iRepo;
    }

    @Override
    public void run() {

        Message sent;
        Object sendValue = null;
        Object[] args;

        switch (received.getMessageID()) {
            default:
                System.out.println("ERRO SHOP");
                break;
            case "updateCraftState":    //
                args = (Object[]) received.getMessageContent();
                iRepo.updateCraftState((int) args[0], (String) args[1]);
                break;
            case "updateCustState": //
                args = (Object[]) received.getMessageContent();
                iRepo.updateCustState((int) args[0], (String) args[1]);
                break;
            case "updateEntrepState":   //
                iRepo.updateEntrepState((String) received.getMessageContent());
                break;
            case "updateShopState":
                iRepo.updateShopState((String) received.getMessageContent());
                break;
            case "productsRemoved":
                iRepo.productsRemoved((int) received.getMessageContent());
                break;
            case "productsBought":
                args = (Object[]) received.getMessageContent();
                iRepo.productsBought((int) args[0], (int) args[0]);
                break;
            case "callForMaterials":
                iRepo.callForMaterials();
                break;
            case "hasCallForMaterials":
                sendValue = (Object) iRepo.hasCallForMaterials();
                break;
            case "callForProducts":
                iRepo.callForProducts();
                break;
            case "hasCallForProducts":
                sendValue = (Object) iRepo.hasCallForProducts();
                break;
            case "supplierHasMaterials":
                sendValue = (Object) iRepo.supplierHasMaterials();
                break;
            case "isDaysWorkEnded":
                sendValue = (Object) iRepo.isDaysWorkEnded();
                break;
            case "customerEnteredStore":
                iRepo.customerEnteredStore();
                break;
            case "customerLeftStore":
                iRepo.customerLeftStore();
                break;
            case "batchDelivered":
                iRepo.batchDelivered((int) received.getMessageContent());
                break;
            case "isAllproductsSold":
                sendValue = (Object) iRepo.isAllproductsSold();
                break;
                

        }
        sent = new Message("ack", received.getMessageID(), sendValue, "repository", 0, received.getSenderType(), received.getSenderID());

        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }
}
