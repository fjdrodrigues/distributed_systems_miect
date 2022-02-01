package interfaces;

/**
 * Represents a set of actions the entrepreneur can do while in the workshop
 *
 * @author ribeiro
 */
public interface IEntrepreWorkShop {

    /**
     * The Entrepreneur goes to the Workshop to collect a batch of products
     *
     * @return the amount of collected products
     */
    int goToWorkShop();            //devolve int, numero de produtos...

    /**
     * The Entrepreneur visits the suppliers in order to get prime materials for
     * the Workshop
     *
     * @return the amount of prime materials to be delivered to the Workshop
     */
    int visitSuppliers();

    /**
     * The Entrepreneur delivers prime materials to the Workshop
     *
     * @param amount the amount of materials delivered to the Workshop
     */
    void replenishStock(int amount);

}
