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
public interface IShop {

    void updateCraftState(int craftsID, String newState);   

    void updateCustState(int custID, String newState);  

    void updateEntrepState(String newState);    

    void updateShopState(String newState);  

    void productsRemoved(int nProducts);        

    void productsBought(int custID, int nProducts); 

    void callForMaterials();    

    boolean hasCallForMaterials();  

    void callForProducts(); 

    boolean hasCallForProducts();   

    boolean supplierHasMaterials();
    
    boolean isDaysWorkEnded();  

    void customerEnteredStore();    

    void customerLeftStore();       

    void batchDelivered(int batchSize);  

    boolean isAllproductsSold();    
}
