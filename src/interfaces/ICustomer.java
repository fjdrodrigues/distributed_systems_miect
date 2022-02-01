package interfaces;

/**
 * Represents a set of actions a customer can do
 *
 * @author ribeiro
 */
public interface ICustomer {

    /**
     * The Customer goes to check if the Shop door is open
     *
     * @param custID the ID of the Customer
     */
    void goShopping(int custID);

    /**
     * Checks if the Shop door is open
     *
     * @return <ul><li>true, if the door is open
     * <li>false, if the door is closed</ul>
     */
    boolean isDoorOpen();

    /**
     * The Shop door was closed, the Customer goes back to his routine
     *
     * @param custID the ID of the Customer
     */
    void tryAgainLater(int custID);

    /**
     * A Customer enters the Shop
     *
     * @param custID the ID of the Customer
     */
    void enterShop(int custID);

    /**
     * Calculates a random amount of products a Customer will be interested in
     *
     * @return the amount of products the Customer wants to buy
     */
    int perusingAround();

    /**
     * A Customer is interested in buying one or more products
     *
     * @param custID the ID of the Customer
     * @param nProducts the amount of products the Customer is interested in
     * buying
     */
    void iWantThis(int custID, int nProducts);

    /**
     * A Customer has exited the Shop and goes back to his routine
     *
     * @param custID the ID of the Customer
     */
    void exitShop(int custID);

    /**
     * Checks if the shop has any products to sell
     *
     * @return <ul><li>true, if there are products in the shop
     * <li>false, if there are none</ul>
     */
    boolean shopHasProducts();              //devolve true se houver produtos na loja

    /**
     * checks if all the products have been made and sold
     *
     * @return <ul><li>true, if there are no more products
     * <li>false, if there are still products</ul>
     */
    boolean isShopClosed();

    /**
     * A Customer has finished all of its tasks
     *
     * @param custID the ID of the Customer
     */
    void custFinished(int custID);
}
