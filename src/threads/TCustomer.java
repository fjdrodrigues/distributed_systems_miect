package threads;

import interfaces.ICustomer;

/**
 * The customer action sequence
 *
 * @author ribeiro
 */
public class TCustomer extends Thread {

    /**
     * reference to the monitor responsible for the Customer
     */
    private final ICustomer iCus;

    /**
     * the ID of the Customer
     */
    private final int custID;

    /**
     * the max amount of time (ns) that the thread can sleep
     */
    private final int maxSleepTime;

    /**
     * @param icus reference to the monitor responsible for the Customer
     * @param custID the ID of the Customer
     * @param maxSleepTime the max amount of time (ns) that the thread can sleep
     */
    public TCustomer(ICustomer icus, int custID, int maxSleepTime) {
        this.iCus = icus;
        this.custID = custID;
        this.maxSleepTime = maxSleepTime;
    }

    /**
     * Runs the thread
     */
    @Override
    public void run() {                                 //enquanto a loja nao estiver fechada
        while (!iCus.isShopClosed()) {
            int nProducts;                              //number of products the client is interested in

            livingNormalLife();
            iCus.goShopping(custID);
            if (!iCus.isDoorOpen()) {
                iCus.tryAgainLater(custID);
            } else {
                iCus.enterShop(custID);
                if (iCus.shopHasProducts()) {                             //se ha produtos na loja
                    nProducts = iCus.perusingAround();
                    if (nProducts > 0) {
                        iCus.iWantThis(custID, nProducts);
                    }
                }
                iCus.exitShop(custID);
            }
        }
        iCus.custFinished(custID);
        System.out.println("Customer " + custID + " finished!");
    }

    /**
     * Sleeps the thread
     */
    private void livingNormalLife() {
        try {
            int sleepTime = (int) (Math.random() * maxSleepTime);
            sleep(sleepTime);
        } catch (InterruptedException ex) {
        }
    }

}
