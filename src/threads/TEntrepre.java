package threads;

import interfaces.IEntrepreWorkShop;
import interfaces.IEntrepreShop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entrepreneur action sequence
 *
 * @author ribeiro
 */
public class TEntrepre extends Thread {

    /**
     * the reference to the monitor responsible for the thread at the Shop
     */
    private final IEntrepreShop iEntrepreShop;

    /**
     * the reference to the monitor responsible for the thread at the workshop
     */
    private final IEntrepreWorkShop iEntrepreWorkShop;

    /**
     * the max amount of time (ns) that the thread can sleep
     */
    private final int maxSleepTime;

    /**
     * @param iEntrepreShop the reference to the monitor responsible for the
     * thread at the Shop
     * @param iEntrepreWorkShop the reference to the monitor responsible for the
     * thread at the workshop
     * @param maxSleepTime the max amount of time (ns) that the thread can sleep
     */
    public TEntrepre(IEntrepreShop iEntrepreShop, IEntrepreWorkShop iEntrepreWorkShop, int maxSleepTime) {
        this.iEntrepreShop = iEntrepreShop;
        this.iEntrepreWorkShop = iEntrepreWorkShop;
        this.maxSleepTime = maxSleepTime;
    }

    /**
     * Runs the thread
     */
    @Override
    public void run() {
        int batchDelivery, materialDelivery;
        while (!iEntrepreShop.isShopClosed()) {              //enquanto ouver produtos na loja ou na oficina ou ainda existir materiais
            batchDelivery = materialDelivery = 0;
            iEntrepreShop.prepareToWork();

            iEntrepreShop.appraiseSit();

            if (iEntrepreShop.hasCustomersWaiting()) {
                iEntrepreShop.addressACustomer();
                serviceCustomer();
                iEntrepreShop.sayGoodByeToCustomer();
            } else {
                iEntrepreShop.closeTheDoor();
                iEntrepreShop.prepareToLeave();
                if (iEntrepreShop.hasCallForMaterials() && iEntrepreShop.supplierHasMaterials()) //se houve uma chamada pra material
                {
                    materialDelivery = iEntrepreWorkShop.visitSuppliers();
                    iEntrepreWorkShop.replenishStock(materialDelivery);
                } else if (iEntrepreShop.hasCallForProducts()) //se houve uma chamada para produtos
                {
                    batchDelivery = iEntrepreWorkShop.goToWorkShop();
                }

                iEntrepreShop.returnToShop(batchDelivery);
            }
        }
        iEntrepreShop.entrepreFinished();
        System.out.println("Entrepreneur finished!");
    }

    /**
     * Sleeps the thread
     */
    public void serviceCustomer() {
        try {
            int sleepTime = (int) (Math.random() * maxSleepTime);
            sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(TEntrepre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
