package clientSide;

import interfaces.ICraftsmanShop;
import interfaces.ICraftsmanWorkShop;
import java.io.IOException;
import message.Message;

/**
 *
 * @author João
 */
public class ClientCraftsman implements ICraftsmanShop, ICraftsmanWorkShop {

    private ClientCom comClientShop;
    private ClientCom comClientWS;

    public ClientCraftsman(String shopIP, String wsIP, int shopPortNumber, int workShopPortNumber) {
        comClientShop = new ClientCom(shopIP, shopPortNumber);
        comClientWS = new ClientCom(wsIP, workShopPortNumber);
    }

    @Override
    public void primeMaterialsNeeded(int craftsID) {
        sendMessage("primeMaterialsNeeded", null, craftsID, "shop");
    }

    @Override
    public void batchReadyForTransfer(int craftsID) {
        sendMessage("batchReadyForTransfer", null, craftsID, "shop");
    }

    @Override
    public boolean checkForMaterials() {
        return (boolean) sendMessage("checkForMaterials", null, 0, "workshop");
    }

    @Override
    public boolean lowMaterialsInWorkshop() {
        return (boolean) sendMessage("lowMaterialsInWorkshop", null, 0, "workshop");
    }

    @Override
    public void collectMaterials(int craftsID) {
        sendMessage("collectMaterials", null, craftsID, "workshop");
    }

    @Override
    public void prepareToProduce(int craftsID) {
        sendMessage("prepareToProduce", null, craftsID, "workshop");
    }

    @Override
    public void goToStore(int craftsID) {
        sendMessage("goToStore", null, craftsID, "workshop");
    }

    @Override
    public void backToWork(int craftsID) {
        sendMessage("backToWork", null, craftsID, "workshop");
    }

    @Override
    public boolean materialsNotDepleted() {
        return (boolean) sendMessage("materialsNotDepleted", null, 0, "workshop");
    }

    @Override
    public boolean isEntrepreCalledMaterials() {
        return (boolean) sendMessage("isEntrepreCalledMaterials", null, 0, "workshop");
    }

    @Override
    public boolean isEntrepreCalledProducts() {
        return (boolean) sendMessage("isEntrepreCalledProducts", null, 0, "workshop");
    }

    @Override
    public boolean isBatchReady() {
        return (boolean) sendMessage("isBatchReady", null, 0, "workshop");
    }

    @Override
    public void craftsFinished(int craftsID) {
        sendMessage("craftsFinished", null, craftsID, "workshop");
    }

    private synchronized Object sendMessage(String action, Object content, int requesterID, String sendertype) {
        Object returnVal;
        ClientCom comClient;

        if (sendertype.equals("shop")) {
            comClient = comClientShop;
        } else {
            comClient = comClientWS;
        }

        //Message msg = new Message(action, content, "craftsman", requesterID, "notification");
        Message msg = new Message("notification", action, content, "craftsman", requesterID, sendertype, 0);
        while (!comClient.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        //System.out.println("SENDING:\n" + msg.toString());
        comClient.writeObject((Object) msg);
        Message reply = (Message) comClient.readObject();

        while (!(reply.getMessageType().equals("ack") && reply.getSenderType().equals(sendertype)
                && reply.getReceiverType().equals("craftsman") && reply.getReceiverID() == requesterID
                && reply.getMessageID().equals(action))) {
            reply = (Message) comClient.readObject();
        }
        //System.out.println("RECEIVING:\n" + reply.toString());

        returnVal = (Object) reply.getMessageContent();
        comClient.close();
        return returnVal;
    }

}
