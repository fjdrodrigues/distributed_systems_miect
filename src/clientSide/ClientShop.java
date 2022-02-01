package clientSide;

import interfaces.IShop;
import message.Message;

/**
 *
 * @author ribeiro
 */
public class ClientShop implements IShop {

    private ClientCom comClient;
    private Object[] args;

    public ClientShop(String ipAddress, int portNumber) {
        comClient = new ClientCom(ipAddress, portNumber);
    }

    @Override
    public synchronized void updateCraftState(int craftsID, String newState) {
        args = new Object[]{(Object) craftsID, (Object) newState};
        sendMessage("updateCraftState", (Object) args);
    }

    @Override
    public synchronized void updateCustState(int custID, String newState) {
        args = new Object[]{(Object) custID, (Object) newState};
        sendMessage("updateCustState", (Object) args);
    }

    @Override
    public synchronized void updateEntrepState(String newState) {
        sendMessage("updateEntrepState", (Object) newState);
    }

    @Override
    public synchronized void updateShopState(String newState) {
        sendMessage("updateShopState", (Object) newState);
    }

    @Override
    public synchronized void productsRemoved(int nProducts) {
        sendMessage("productsRemoved", (Object) nProducts);
    }

    @Override
    public synchronized void productsBought(int custID, int nProducts) {
        args = new Object[]{(Object) custID, (Object) nProducts};
        sendMessage("productsBought", (Object) args);
    }

    @Override
    public synchronized void callForMaterials() {
        sendMessage("callForMaterials", null);
    }

    @Override
    public synchronized boolean hasCallForMaterials() {
        return (boolean) sendMessage("hasCallForMaterials", null);
    }

    @Override
    public synchronized void callForProducts() {
        sendMessage("callForProducts", null);
    }

    @Override
    public synchronized boolean hasCallForProducts() {
        return (boolean) sendMessage("hasCallForProducts", null);
    }

    @Override
    public synchronized boolean supplierHasMaterials() {
        return (boolean) sendMessage("supplierHasMaterials", null);
    }

    @Override
    public synchronized boolean isDaysWorkEnded() {
        return (boolean) sendMessage("isDaysWorkEnded", null);
    }

    @Override
    public synchronized void customerEnteredStore() {
        sendMessage("customerEnteredStore", null);
    }

    @Override
    public synchronized void customerLeftStore() {
        sendMessage("customerLeftStore", null);
    }

    @Override
    public synchronized void batchDelivered(int batchSize) {
        sendMessage("batchDelivered", (Object) batchSize);
    }

    @Override
    public synchronized boolean isAllproductsSold() {
        return (boolean) sendMessage("isAllproductsSold", null);
    }

    private synchronized Object sendMessage(String action, Object content) {
        Object returnVal;

        //Message msg = new Message(action, content, "shop", 0, "notification");
        Message msg = new Message("notification", action, content, "shop", 0, "repository", 0);

        while (!comClient.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }        //System.out.println("SENDING:\n" + msg.toString());
        comClient.writeObject(msg);
        Message reply = (Message) comClient.readObject();

        while (!(reply.getMessageType().equals("ack") && reply.getSenderType().equals("repository")
                && reply.getReceiverType().equals("shop") && reply.getMessageID().equals(action))) {
            reply = (Message) comClient.readObject();
        }
        //System.out.println("RECEIVING:\n" + reply.toString());

        returnVal = (Object) reply.getMessageContent();

        comClient.close();

        return returnVal;
    }

}
