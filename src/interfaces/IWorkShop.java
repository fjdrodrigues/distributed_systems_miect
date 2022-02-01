/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author ribeiro
 */
public interface IWorkShop {

    void updateCraftState(int craftsID, String newState);

    void updateEntrepState(String newState);

    void productCrafted(int craftsID);

    void materialRemoved();

    boolean hasCallForMaterials();

    boolean hasCallForProducts();

    boolean supplierHasMaterials();

    int suppliersRealMaterials();

    boolean isDaysWorkEnded();

    void batchCollected(int batchSize);

    void suppliersVisited(int amount);

    void materialsDelivered(int amount);

    int getWorkShopAPMI();
}
