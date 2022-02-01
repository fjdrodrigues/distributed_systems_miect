package interfaces;

/**
 * Represents a set of actions the entrepreneur can do while in the shop
 *
 * @author ribeiro
 */
public interface IEntrepreShop {

    /**
     * The Entrepreneur is preparing to work
     */
    void prepareToWork();

    /**
     * The Entrepreneur is waiting for his next task
     */
    void appraiseSit();

    /**
     * The Entrepreneur closes the Shop door
     */
    void closeTheDoor();

    /**
     * Checks if the Shop has Customers waiting to pay
     *
     * @return <ul><li>true, if the Shop has Customers waiting
     * <li>false, if it has no Customers waiting</ul>
     */
    boolean hasCustomersWaiting();

    /**
     * The Entrepreneur addresses a Customer
     */
    void addressACustomer();

    /**
     * The Entrepreneur has serviced the Customer, notifies it and says goodbye
     */
    void sayGoodByeToCustomer();

    /**
     * The Entrepreneur is preparing to close the Shop
     */
    void prepareToLeave();

    /**
     * The Entrepreneur returns to the Shop and opens it and may carry or not a
     * batch of products from the Workshop
     *
     * @param batchSize the amount of products brought from the Workshop
     */
    void returnToShop(int batchSize);

    /**
     * Checks if a call has been made for material delivery to the Workshop
     *
     * @return <ul><li>true, if a call has been made
     * <li>false, if no call has been made</ul>
     */
    boolean hasCallForMaterials();         //devolve true se houver 1 chamada para materials

    /**
     * Checks if a call has been made for product delivery to the Shop
     *
     * @return <ul><li>true, if a call has been made
     * <li>false, if no call has been made</ul>
     */
    boolean hasCallForProducts();          //devolve true se houver 1 chamada para produtos feitos

    /**
     * Checks if the supplier has materials left to be delivered
     *
     * @return <ul><li>true, if the supplier still has materials left
     * <li>false, if the supplier has no materials left to be delivered</ul>
     */
    boolean supplierHasMaterials();

    /**
     * Checks if the Shop is Closed
     *
     * @return <ul><li>true, if the Shop is closed
     * <li>false, if it is open</ul>
     */
    boolean isShopClosed();

    /**
     * The Entrepreneur has finished all of his tasks
     */
    void entrepreFinished();

}
