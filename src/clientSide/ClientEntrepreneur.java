package clientSide;

import interfaces.IEntrepreShop;
import interfaces.IEntrepreWorkShop;
import java.io.IOException;
import message.Message;

/**
 *
 * @author ribeiro
 */
public class ClientEntrepreneur implements IEntrepreShop, IEntrepreWorkShop {

    private ClientCom comClientShop;
    private ClientCom comClientWS;

    public ClientEntrepreneur(String shopIP, String wsIP, int shopPortNumber, int workShopPortNumber) {
        comClientShop = new ClientCom(shopIP, shopPortNumber);
        comClientWS = new ClientCom(wsIP, workShopPortNumber);
    }

    /* SHOP */
    @Override
    public synchronized void prepareToWork() {
        sendMessage("prepareToWork", null, "shop");
    }

    @Override
    public synchronized void appraiseSit() {
        sendMessage("appraiseSit", null, "shop");
    }

    @Override
    public synchronized void closeTheDoor() {
        sendMessage("closeTheDoor", null, "shop");
    }

    @Override
    public synchronized boolean hasCustomersWaiting() {
        return (boolean) sendMessage("hasCustomersWaiting", null, "shop");
    }

    @Override
    public synchronized void addressACustomer() {
        sendMessage("addressACustomer", null, "shop");
    }

    @Override
    public synchronized void sayGoodByeToCustomer() {
        sendMessage("sayGoodByeToCustomer", null, "shop");
    }

    @Override
    public synchronized void prepareToLeave() {
        sendMessage("prepareToLeave", null, "shop");
    }

    @Override
    public synchronized void returnToShop(int batchSize) {
        sendMessage("returnToShop", (Object) batchSize, "shop");
    }

    @Override
    public synchronized boolean hasCallForMaterials() {
        return (boolean) sendMessage("hasCallForMaterials", null, "shop");
    }

    @Override
    public synchronized boolean hasCallForProducts() {
        return (boolean) sendMessage("hasCallForProducts", null, "shop");
    }

    @Override
    public synchronized boolean supplierHasMaterials() {
        return (boolean) sendMessage("supplierHasMaterials", null, "shop");
    }

    @Override
    public synchronized boolean isShopClosed() {
        return (boolean) sendMessage("isShopClosed", null, "shop");
    }

    @Override
    public synchronized void entrepreFinished() {
        sendMessage("entrepreFinished", null, "shop");
    }

    /* WORKSHOP */
    @Override
    public synchronized int goToWorkShop() {
        return (int) sendMessage("goToWorkShop", null, "workshop");
    }

    @Override
    public synchronized int visitSuppliers() {
        return (int) sendMessage("visitSuppliers", null, "workshop");
    }

    @Override
    public synchronized void replenishStock(int amount) {
        sendMessage("replenishStock", (Object) amount, "workshop");
    }

    private synchronized Object sendMessage(String action, Object content, String serverType) {
        ClientCom comClient;
        if (serverType.equals("shop")) {
            comClient = comClientShop;
        } else {
            comClient = comClientWS;
        }
        Object returnVal;
        Message msg = new Message("notification", action, content, "entrepreneur", 0, serverType, 0);
        while (!comClient.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }        
//System.out.println("SENDING:\n" + msg.toString());
        comClient.writeObject(msg);
        Message reply = (Message) comClient.readObject();

        while (!(reply.getMessageType().equals("ack") && reply.getSenderType().equals(serverType)
                && reply.getReceiverType().equals("entrepreneur") && reply.getMessageID().equals(action))) {
            reply = (Message) comClient.readObject();
        }
//System.out.println("RECEIVING:\n" + reply.toString());

        returnVal = (Object) reply.getMessageContent();
        comClient.close();
        return returnVal;
    }
}
