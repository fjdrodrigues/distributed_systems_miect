package interfaces;

/**
 * Represents a set of actions a craftsman can do while in the shop
 *
 * @author ribeiro
 */
public interface ICraftsmanShop {

    /**
     * A Craftsman is requesting materials to the Entrepreneur (notifies it)
     *
     * @param craftsID the ID of the craftsman
     */
    void primeMaterialsNeeded(int craftsID);

    /**
     * A Craftsman is requesting products to be transfered from the Workshop to
     * the Shop, notifies the Entrepreneur
     *
     * @param craftsID the ID of the Craftsman
     */
    void batchReadyForTransfer(int craftsID);
}
