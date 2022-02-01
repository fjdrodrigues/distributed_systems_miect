package threads;

import interfaces.ICraftsmanWorkShop;
import interfaces.ICraftsmanShop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The craftsman action sequence
 *
 * @author ribeiro
 */
public class TCraftsman extends Thread {

    /**
     * reference to the monitor responsible for the thread at the Workshop
     */
    private final ICraftsmanWorkShop iCrafts;

    /**
     * reference to the monitor responsible for the thread at the Shop
     */
    private final ICraftsmanShop iCraftsShop;

    /**
     * the ID of the craftsman
     */
    private final int craftsID;

    /**
     * the max amount of time (ns) that the thread can sleep
     */
    private final int maxSleepTime;

    /**
     * @param iCrafts reference to the monitor responsible for the thread at the
     * Workshop
     * @param iCraftsShop reference to the monitor responsible for the thread at
     * the Shop
     * @param craftsID the ID of the craftsman
     * @param maxSleepTime the max amount of time (ns) that the thread can sleep
     */
    public TCraftsman(ICraftsmanWorkShop iCrafts, ICraftsmanShop iCraftsShop, int craftsID, int maxSleepTime) {
        this.iCrafts = iCrafts;
        this.iCraftsShop = iCraftsShop;
        this.craftsID = craftsID;
        this.maxSleepTime = maxSleepTime;
    }

    /**
     * Runs the thread
     */
    @Override
    public void run() {
        boolean primeMaterialsRequired;
        while (iCrafts.materialsNotDepleted()) {              //condicao para correr -> enquanto houver materiais
            primeMaterialsRequired = iCrafts.lowMaterialsInWorkshop() && !iCrafts.isEntrepreCalledMaterials();
            if (primeMaterialsRequired) {          //se ninguem tiver ido
                iCraftsShop.primeMaterialsNeeded(craftsID);
                iCrafts.backToWork(craftsID);                           //mudar
            } else {
                if (iCrafts.checkForMaterials()) {
                    iCrafts.collectMaterials(craftsID);
                    iCrafts.prepareToProduce(craftsID);
                    shapingItUp();
                    iCrafts.goToStore(craftsID);
                    if (iCrafts.isBatchReady() && !iCrafts.isEntrepreCalledProducts()) //se esta na capacidade maxima
                    {
                        iCraftsShop.batchReadyForTransfer(craftsID);
                    }
                    iCrafts.backToWork(craftsID);                           
                }
            }
        }
        primeMaterialsRequired = iCrafts.lowMaterialsInWorkshop() && !iCrafts.isEntrepreCalledMaterials();
        if (primeMaterialsRequired) {          //se ninguem tiver ido
            iCraftsShop.primeMaterialsNeeded(craftsID);
            iCrafts.backToWork(craftsID);
        }
        iCrafts.craftsFinished(craftsID);
        System.out.println("Craftsman " + craftsID + " finished!");
    }

    /**
     * Sleeps the thread
     */
    private void shapingItUp() {
        try {
            int sleepTime = (int) (Math.random() * maxSleepTime);
            sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(TCraftsman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
