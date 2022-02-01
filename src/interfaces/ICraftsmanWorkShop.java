package interfaces;

/**
 * Represents a set of actions a craftsman can do while in the workshop.
 *
 * @author ribeiro
 */
public interface ICraftsmanWorkShop {

    /**
     * Checks if there is enough materials in the Workshop to create 1 product
     *
     * @return <ul><li>true, if there is enough materials
     * <li>false, if there is not</ul>
     */
    boolean checkForMaterials();

    /**
     * Checks if there are enough materials in the Workshop so that each
     * craftsman can still make one product simplified: checks if it is
     * necessary to ask the Entrepreneur for materials
     *
     * @return <ul><li>true, if there is enough materials
     * <li>false, if there is not</ul>
     */
    boolean lowMaterialsInWorkshop();

    /**
     * A Craftsman has collected materials in order to start the manufacturing
     * process
     *
     * @param craftsID the ID of the Craftsman
     */
    void collectMaterials(int craftsID);

    /**
     * The Craftsman is preparing to produce the product
     *
     * @param craftsID the ID of the Craftsman
     */
    void prepareToProduce(int craftsID);

    /**
     * The Craftsman goes to the storing area to store a finished product
     *
     * @param craftsID the ID of the Craftsman
     */
    void goToStore(int craftsID);

    /**
     * The Craftsman goes back to work
     *
     * @param craftsID the ID of the Craftsman
     */
    void backToWork(int craftsID);

    /**
     * Checks if there are any materials left everywhere
     *
     * @return <ul><li>true, if there are still materials
     * <li>false, if there are not</ul>
     */
    boolean materialsNotDepleted();

    /**
     * Checks if the Entrepreneur has been called to deliver materials to the
     * Workshop
     *
     * @return <ul><li>true, if the Entrepreneur has been called
     * <li>false, if he has not</ul>
     */
    boolean isEntrepreCalledMaterials();    //devolve true se o entrepreneur tiver sido chamado para materials

    /**
     * Checks if the Entrepreneur has been called to collect a batch of products
     * from the Workshop
     *
     * @return <ul><li>true, if the Entrepreneur has been called
     * <li>false, if he has not</ul>
     */
    boolean isEntrepreCalledProducts();     //devolve true se o entrepreneur tiver sido chamado pra produtos

    /**
     * Checks if the there are enough finished products at the Workshop to make
     * a batch to send to the Shop
     *
     * @return <ul><li>true, if there are enough products
     * <li>false, if there are not</ul>
     */
    boolean isBatchReady();                 //devolve true se estiverem o maximo de produtos na oficina

    /**
     * A Craftsman has finished all of its tasks
     *
     * @param craftsID the ID of the Craftsman
     */
    void craftsFinished(int craftsID);

}
