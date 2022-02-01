package monitors;

import interfaces.IEntrepreWorkShop;
import interfaces.ICraftsmanWorkShop;
import interfaces.IWorkShop;

/**
 * Monitor relative to the Workshop responsible for managing threads interacting
 * with the Workshop at present time
 *
 * @author ribeiro
 */
public class MWorkShop implements ICraftsmanWorkShop, IEntrepreWorkShop {

    /**
     * the number of craftsman threads
     */
    private final int nCraftsman;

    /**
     * The current state of the Craftsmen
     */
    private final String[] craftsStat;

    /**
     * The number of products (accumulation) manufactured by the craftsman
     */
    private final int[] craftsPP;

    /**
     * The current state of the Entrepreneur
     */
    private String entrepreStat;

    /**
     * amount of prime materials presently in the workshop
     */
    private int workshopAPMI;

    /**
     * number of finished products presently in the workshop
     */
    private int workshopNPI;

    /**
     * the cost of each product to make in materials
     */
    private final int productCost;

    /**
     * the amount of materials in each material delivery to the Workshop
     */
    private final int materialDeliverySize;

    /**
     * the minimum amount of products in each product delivery to the Shop
     */
    private final int batchSize;

    /**
     * reference to the information repository
     */
    private final IWorkShop iWorkShop;

    /**
     * the number of craftsman waiting for the arrival of materials
     */
    private int nCraftsmanWaiting;

    /**
     * @param iWorkShop reference to the information repository
     * @param nCraftsman the number of craftsman threads
     * @param productCost the cost of each product to make in materials
     * @param materialDeliverySize the amount of materials in each material
     * delivery to the Workshop
     * @param batchSize the minimum amount of products in each product delivery
     * to the Shop
     */
    public MWorkShop(IWorkShop iWorkShop, int nCraftsman, int productCost, int materialDeliverySize, int batchSize) {
        this.nCraftsman = nCraftsman;
        this.iWorkShop = iWorkShop;
        this.productCost = productCost;
        this.materialDeliverySize = materialDeliverySize;
        this.batchSize = batchSize;

        craftsStat = new String[nCraftsman];
        craftsPP = new int[nCraftsman];

        for (int i = 0; i < nCraftsman; i++) {
            craftsStat[i] = "FETCHING_PRIME_MATERIALS";
            craftsPP[i] = 0;
        }
        workshopAPMI = workshopNPI = nCraftsmanWaiting = 0;

    }

    /* CRAFTSMEN */
    @Override
    public synchronized boolean checkForMaterials() {
        if (nCraftsmanWaiting < (workshopAPMI / productCost) || !iWorkShop.isDaysWorkEnded()) {
            nCraftsmanWaiting++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean lowMaterialsInWorkshop() {
        if (workshopAPMI <= nCraftsman * productCost) //se tiver materiais necessários nao precisa de fazer verificacao ao repositorio
        {
            return true;
        } else {
            workshopAPMI = iWorkShop.getWorkShopAPMI();
            return workshopAPMI <= nCraftsman * productCost;
        }
    }

    @Override
    public synchronized void collectMaterials(int craftsID) {
        try {
            while (workshopAPMI < productCost) {

                wait();
                /*
                if (!iWorkShop.supplierHasMaterials() && !checkForMaterials()) {
                    break;
                }*/

            }
        } catch (InterruptedException ex) {
        }

        workshopAPMI -= productCost;
        nCraftsmanWaiting--;
        iWorkShop.materialRemoved();
    }

    @Override
    public synchronized void prepareToProduce(int craftsID) {
        craftsStat[craftsID] = "PRODUCING_A_NEW_PIECE";
        iWorkShop.updateCraftState(craftsID, craftsStat[craftsID]);
    }

    @Override
    public synchronized void goToStore(int craftsID) {
        craftsStat[craftsID] = "STORING_IT_FOR_TRANSFER";
        iWorkShop.updateCraftState(craftsID, craftsStat[craftsID]);
        craftsPP[craftsID]++;
        workshopNPI++;
        iWorkShop.productCrafted(craftsID);
    }

    @Override
    public synchronized void backToWork(int craftsID) {
        craftsStat[craftsID] = "FETCHING_PRIME_MATERIALS";
        iWorkShop.updateCraftState(craftsID, craftsStat[craftsID]);
    }

    @Override
    public synchronized boolean materialsNotDepleted() {
        return nCraftsmanWaiting < ((workshopAPMI +iWorkShop.suppliersRealMaterials()) / productCost);
    }

    @Override
    public synchronized boolean isEntrepreCalledMaterials() {
        return iWorkShop.hasCallForMaterials();
    }

    @Override
    public synchronized boolean isEntrepreCalledProducts() {
        return iWorkShop.hasCallForProducts();
    }

    @Override
    public synchronized boolean isBatchReady() {
        return workshopNPI >= batchSize;
    }

    @Override
    public synchronized void craftsFinished(int craftsID) {
        craftsStat[craftsID] = "DEAD";
        iWorkShop.updateCraftState(craftsID, "DEAD");

    }

    /* ENTREPRENEUR */
    @Override
    public synchronized int goToWorkShop() {
        int nbatchSize;

        nbatchSize = workshopNPI;
        workshopNPI = 0;

        iWorkShop.batchCollected(nbatchSize);

        entrepreStat = "COLLECTING_A_BATCH_OF_PRODUCTS";
        iWorkShop.updateEntrepState(entrepreStat);

        return nbatchSize;
    }

    @Override
    public synchronized int visitSuppliers() {
        iWorkShop.suppliersVisited(materialDeliverySize);

        entrepreStat = "BUYING_PRIME_MATERIALS";
        iWorkShop.updateEntrepState(entrepreStat);
        return materialDeliverySize;
    }

    @Override
    public synchronized void replenishStock(int amount) {
        workshopAPMI += amount;
        iWorkShop.materialsDelivered(amount);

        entrepreStat = "DELIVERING_PRIME_MATERIALS";
        iWorkShop.updateEntrepState(entrepreStat);

        notifyAll();
    }

}
