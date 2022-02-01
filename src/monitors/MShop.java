package monitors;

import interfaces.ICraftsmanShop;
import interfaces.IEntrepreShop;
import interfaces.ICustomer;
import interfaces.IShop;

/**
 * Monitor relative to the Shop, responsible for managing threads interacting
 * with the Shop at present time
 *
 * @author ribeiro
 */
public class MShop implements ICustomer, IEntrepreShop, ICraftsmanShop {

    /**
     * the number of customer threads
     */
    private final int nCustomers;

    /**
     * max purchases per Customer in one Shop visit
     */
    private final int maxPurchases;

    /**
     * The current state of the Customers
     */
    private final String[] custStat;

    /**
     * The number of goods (accumulation) bought by the customer
     */
    private final int[] custBP;

    /**
     * The current state of the Entrepreneur
     */
    private String entrepreStat;

    /**
     * The current state of the Shop
     */
    private String shopStat;

    /**
     * number of customers inside the Shop
     */
    private int shopNCI;

    /**
     * number of goods in display at the Shop
     */
    private int shopNPI;

    /**
     * the manager(ReentrantLock) for the customer waiting line (buying
     * products)
     */
    private final RegisterLine registerLine;

    /**
     * Reference to the repository monitor
     */
    private final IShop iShop;

    /**
     * @param iShop reference to the information repository
     * @param registerLine the manager(ReentrantLock) for the customer waiting
     * line (buying products)
     * @param nCustomers the number of customer threads
     * @param maxPurchases the maximum amount of products a Customer can buy at
     * one trip to the Store
     */
    public MShop(IShop iShop, RegisterLine registerLine, int nCustomers, int maxPurchases) {        //Queue waitinQueue
        this.iShop = iShop;
        this.registerLine = registerLine;
        this.nCustomers = nCustomers;
        this.maxPurchases = maxPurchases;
        custStat = new String[nCustomers];
        custBP = new int[nCustomers];
        for (int i = 0; i < nCustomers; i++) {
            custStat[i] = "CARRYING_OUT_DAILY_CHORES";
            custBP[i] = 0;
        }
        entrepreStat = "OPENING_THE_SHOP";
        shopStat = "OP";
        shopNCI = shopNPI = 0;
    }

    @Override
    public synchronized void prepareToWork() {
        entrepreStat = "WAITING_FOR_NEXT_TASK";
        iShop.updateEntrepState("WAITING_FOR_NEXT_TASK");
    }

    @Override
    public synchronized void appraiseSit() {
        try {
            while (!(iShop.hasCallForMaterials() && iShop.supplierHasMaterials()) && !iShop.hasCallForProducts()
                    && !hasCustomersWaiting() && !iShop.isDaysWorkEnded()) {
                wait();
            }
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public synchronized void closeTheDoor() {
        shopStat = "OPDC";
        iShop.updateShopState("OPDC");
    }

    @Override
    public boolean hasCustomersWaiting() {
        return !registerLine.isEmpty();
    }

    @Override
    public void addressACustomer() {
        synchronized (this) {
            entrepreStat = "ATTENDING_A_CUSTOMER";
            iShop.updateEntrepState("ATTENDING_A_CUSTOMER");
            
        }
        registerLine.removeFirstInLine();
    }

    @Override
    public synchronized void sayGoodByeToCustomer() {
        entrepreStat = "WAITING_FOR_NEXT_TASK";
        iShop.updateEntrepState("WAITING_FOR_NEXT_TASK");
    }

    @Override
    public synchronized void prepareToLeave() {
        entrepreStat = "CLOSING_THE_SHOP";
        iShop.updateEntrepState("CLOSING_THE_SHOP");
    }

    @Override
    public synchronized void returnToShop(int batchSize) {
        if (batchSize > 0) {
            shopNPI += batchSize;
            iShop.batchDelivered(batchSize);
        }

        if (iShop.isAllproductsSold()) {
            shopStat = "CD";
        } else {
            shopStat = "OP";
        }
        iShop.updateShopState(shopStat);

        entrepreStat = "OPENING_THE_SHOP";
        iShop.updateEntrepState("OPENING_THE_SHOP");
    }

    @Override
    public synchronized boolean hasCallForMaterials() {
        return iShop.hasCallForMaterials();
    }

    @Override
    public synchronized boolean hasCallForProducts() {
        return iShop.hasCallForProducts();
    }

    @Override
    public synchronized boolean supplierHasMaterials() {
        return iShop.supplierHasMaterials();
    }

    @Override
    public synchronized boolean isShopClosed() {
        return shopStat.equals("CD");
    }

    @Override
    public synchronized void entrepreFinished() {
        entrepreStat = "DEAD";
        iShop.updateEntrepState("DEAD");
    }

    /* CUSTOMER */
    @Override
    public synchronized void goShopping(int custID) {
        custStat[custID] = "CHECKING_DOOR_OPEN";
        iShop.updateCustState(custID, "CHECKING_DOOR_OPEN");
    }

    @Override
    public synchronized boolean isDoorOpen() {
        return shopStat.equals("OP");
    }

    @Override
    public synchronized void tryAgainLater(int custID) {
        custStat[custID] = "CARRYING_OUT_DAILY_CHORES";
        iShop.updateCustState(custID, "CARRYING_OUT_DAILY_CHORES");
    }

    @Override
    public synchronized void enterShop(int custID) {            //mudar
        shopNCI++;
        iShop.customerEnteredStore();
        custStat[custID] = "APPRAISING_OFFER_IN_DISPLAY";
        iShop.updateCustState(custID, "APPRAISING_OFFER_IN_DISPLAY");
    }

    @Override
    public synchronized int perusingAround() {
        int prodsToBuy = (int) (Math.random() * maxPurchases);
        if (prodsToBuy > shopNPI) {
            return -1;
        } else {
            shopNPI -= prodsToBuy;
            iShop.productsRemoved(prodsToBuy);
            return prodsToBuy;
        }
    }

    @Override
    public void iWantThis(int custID, int nProducts) {
        synchronized (this) {
            custStat[custID] = "BUYING_SOME_GOODS";
            iShop.updateCustState(custID, "BUYING_SOME_GOODS");

            if (entrepreStat.equals("WAITING_FOR_NEXT_TASK")) {
                notify();
            }
            
        }
        registerLine.enterLine();
        synchronized (this) {
            custBP[custID] += nProducts;

            iShop.productsBought(custID, nProducts);
        }

    }

    @Override
    public synchronized void exitShop(int custID) {     ///mudar
        shopNCI--;
        iShop.customerLeftStore();
        custStat[custID] = "CARRYING_OUT_DAILY_CHORES";
        iShop.updateCustState(custID, "CARRYING_OUT_DAILY_CHORES");

        if (entrepreStat.equals("WAITING_FOR_NEXT_TASK")) {
            notify();           //acorda dona da empresa
        }
    }

    @Override
    public synchronized boolean shopHasProducts() {
        return shopNPI > 0;
    }

    @Override
    public synchronized void custFinished(int custID) {
        custStat[custID] = "DEAD";
        iShop.updateCustState(custID, "DEAD");
    }

    /* CRAFTSMAN */
    @Override
    public synchronized void primeMaterialsNeeded(int craftsID) {
        iShop.updateCraftState(craftsID, "CONTACTING_THE_ENTREPRENEUR");
        iShop.callForMaterials();

        notify();
    }

    @Override
    public synchronized void batchReadyForTransfer(int craftsID) {
        iShop.updateCraftState(craftsID, "CONTACTING_THE_ENTREPRENEUR");
        iShop.callForProducts();

        notify();                   //acorda a dona da empresa
    }

}
