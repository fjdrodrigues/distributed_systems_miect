package proxys;

import interfaces.IWorkShop;
import message.Message;
import serverSide.ServerCom;

/**
 *
 * @author ribeiro
 */
public class TWSRepo extends Thread {

    private final IWorkShop iRepo;
    private final ServerCom serverSocket;
    private final Message received;

    public TWSRepo(ServerCom serverSocket, Message received, IWorkShop mRepo) {
        this.iRepo = mRepo;
        this.serverSocket = serverSocket;
        this.received = received;
    }

    @Override
    public void run() {

        Message sent;
        Object sendValue = null;
        Object[] args;

        switch (received.getMessageID()) {
            default:
                System.out.println("ERRO WS");
                break;
            case "updateCraftState":
                args = (Object[]) received.getMessageContent();
                iRepo.updateCraftState((int) args[0], (String) args[1]);
                break;
            case "updateEntrepState":
                iRepo.updateEntrepState((String) received.getMessageContent());
                break;
            case "productCrafted":
                iRepo.productCrafted((int) received.getMessageContent());
                break;
            case "materialRemoved":
                iRepo.materialRemoved();
                break;
            case "hasCallForMaterials":
                sendValue = (Object) iRepo.hasCallForMaterials();
                break;
            case "suppliersRealMaterials":
                sendValue = (Object) iRepo.suppliersRealMaterials();
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
            case "batchCollected":
                iRepo.batchCollected((int) received.getMessageContent());
                break;
            case "suppliersVisited":
                iRepo.suppliersVisited((int) received.getMessageContent());
                break;
            case "materialsDelivered":
                iRepo.materialsDelivered((int) received.getMessageContent());
                break;
            case "getWorkShopAPMI":
                sendValue = (Object) iRepo.getWorkShopAPMI();
                break;
        }
        sent = new Message("ack", received.getMessageID(), sendValue, "repository", 0, received.getSenderType(), received.getSenderID());

        //System.out.println("SENDING:\n" + sent.toString());
        serverSocket.writeObject(sent);
    }
}
