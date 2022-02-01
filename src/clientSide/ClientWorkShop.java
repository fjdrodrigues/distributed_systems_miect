/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

//import interfaces.IWorkShop;
import interfaces.IWorkShop;
import message.Message;

/**
 *
 * @author João
 */
public class ClientWorkShop implements IWorkShop {

    private ClientCom comClient;
    private Object[] args;

    public ClientWorkShop(String ipAddress, int portNumber) {
        comClient = new ClientCom(ipAddress, portNumber);
    }

    @Override
    public void updateCraftState(int craftsID, String newState) {
        args = new Object[]{(Object) craftsID, (Object) newState};
        sendMessage("updateCraftState", args);
    }

    @Override
    public void updateEntrepState(String newState) {
        sendMessage("updateEntrepState", (Object) newState);
    }

    @Override
    public void productCrafted(int craftsID) {
        sendMessage("productCrafted", (Object) craftsID);
    }

    @Override
    public void materialRemoved() {
        sendMessage("materialRemoved", null);
    }

    @Override
    public boolean hasCallForMaterials() {
        return (boolean) sendMessage("hasCallForMaterials", null);
    }

    @Override
    public boolean hasCallForProducts() {
        return (boolean) sendMessage("hasCallForProducts", null);
    }

    @Override
    public boolean supplierHasMaterials() {
        return (boolean) sendMessage("supplierHasMaterials", null);
    }

    @Override
    public int suppliersRealMaterials() {
        return (int) sendMessage("suppliersRealMaterials", null);
    }

    @Override
    public boolean isDaysWorkEnded() {
        return (boolean) sendMessage("isDaysWorkEnded", null);
    }

    @Override
    public void batchCollected(int batchSize) {
        sendMessage("batchCollected", (Object) batchSize);
    }

    @Override
    public void suppliersVisited(int amount) {
        sendMessage("suppliersVisited", (Object) amount);
    }

    @Override
    public void materialsDelivered(int amount) {
        sendMessage("materialsDelivered", (Object) amount);
    }

    @Override
    public int getWorkShopAPMI() {
        return (int) sendMessage("getWorkShopAPMI", null);
    }

    private synchronized Object sendMessage(String action, Object content) {
        Object returnVal;
        Message msg = new Message("notification", action, content, "workshop", 0, "repository", 0);
        while (!comClient.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }        //System.out.println("SENDING:\n" + msg.toString());
        comClient.writeObject(msg);
        Message reply = (Message) comClient.readObject();

        while (!(reply.getMessageType().equals("ack") && reply.getSenderType().equals("repository")
                && reply.getReceiverType().equals("workshop") && reply.getMessageID().equals(action))) {
            reply = (Message) comClient.readObject();
        }

        //System.out.println("RECEIVING:\n" + reply.toString());
        returnVal = (Object) reply.getMessageContent();
        comClient.close();
        return returnVal;
    }
}
