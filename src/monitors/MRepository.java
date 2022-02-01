package monitors;

import interfaces.IShop;
import interfaces.IWorkShop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Repository holds the status information of the problem at hand
 *
 * @author ribeiro
 */
public class MRepository implements IShop, IWorkShop{

    /**
     * the number of customer threads
     */
    private final int nCustomers;

    /**
     * the number of craftsman threads
     */
    private final int nCraftsmans;

    /**
     * The current state of the Customers
     */
    private String[] custStat;

    /**
     * The current state of the Craftsmen
     */
    private String[] craftStat;

    /**
     *
     * The current state of the Entrepreneur
     */
    private String entrepreStat;

    /**
     * The number of goods (accumulation) bought by the customer
     */
    private int[] custBP;

    /**
     * The number of products (accumulation) manufactured by the craftsman
     */
    private int[] craftPP;

    /**
     * The state of the shop (OP-open / OPDC-still open, but its door is closed
     * /CL- closed)
     */
    private String shopStat;

    /**
     * The number of customers inside the Shop
     */
    private int shopNCI;

    /**
     * The number of goods in display in the Shop
     */
    private int shopNPI;

    /**
     * A phone call was made by a craftsman requesting the transfer of finished
     * products to the shop
     */
    private boolean shopPCR;

    /**
     * A phone call was made by a craftsman requesting the supply of prime
     * materials to the workshop
     */
    private boolean shopPMR;

    /**
     * The amount of prime materials presently in the workshop
     */
    private int workshopAPMI;

    /**
     * number of finished products presently in the workshop
     */
    private int workshopNPI;

    /**
     * number of times that a supply of prime materials was delivered to the
     * workshop
     */
    private int workshopNSPM;

    /**
     * total amount of prime materials that have already been supplied
     * (accumulation)
     */
    private int workshopTAPM;

    /**
     * total number of products that have already been manufactured
     * (accumulation)
     */
    private int workshopTNP;

    /**
     * the cost of materials to create a product
     */
    private final int productCost;

    /**
     * the size (in products) of each material delivery to the workshop
     */
    private final int materialDeliverySize;

    /**
     * the minimum size (in products) of each product delivery from the workshop
     * to the shop
     */
    private final int batchCapacity;

    /**
     * Stream to write in the log file
     */
    private FileOutputStream fos;

    /**
     * the amount of materials the supplier has
     */
    private int totalMaterial;

    /**
     * @param nCustomers the number of customer threads
     * @param nCraftsmans the number of craftsman threads
     * @param product_cost the cost of each product to produce
     * @param materialDeliverySize the size (in products) of each material
     * delivery to the workshop
     * @param batchCapacity the minimum size (in products) of each product
     * delivery from the workshop to the shop
     * @param totalMaterial the amount of materials the supplier has
     * @param logFileName the file path to the log file
     */
    public MRepository(int nCustomers, int nCraftsmans, int product_cost, int materialDeliverySize, int batchCapacity,
            int totalMaterial, String logFileName) {
        this.nCustomers = nCustomers;
        this.nCraftsmans = nCraftsmans;
        this.productCost = product_cost;
        this.materialDeliverySize = materialDeliverySize;
        this.batchCapacity = batchCapacity;
        this.totalMaterial = totalMaterial;

        custStat = new String[nCustomers];
        craftStat = new String[nCraftsmans];

        custBP = new int[nCustomers];
        craftPP = new int[nCraftsmans];

        for (int i = 0; i < nCustomers; i++) {
            custStat[i] = "CARRYING_OUT_DAILY_CHORES";
            custBP[i] = 0;
        }

        for (int i = 0; i < nCraftsmans; i++) {
            craftStat[i] = "FETCHING_PRIME_MATERIALS";
            craftPP[i] = 0;
        }

        entrepreStat = "OPENING_THE_SHOP";
        shopStat = "OP";
        shopNCI = 0;
        shopNPI = 0;
        shopPCR = false;
        shopPMR = false;
        workshopAPMI = 0;
        workshopNPI = 0;
        workshopNSPM = 0;
        workshopTAPM = 0;
        workshopTNP = 0;

        try {
            fos = new FileOutputStream(new File(logFileName));
            fos.write(("\n                         Aveiro Handicraft SARL - Description of the internal"
                    + " state\n").getBytes());
            fos.write(String.format("\n------Problem input values:"
                    + "\nProduct cost (material amount) = %d"
                    + "\nAmount of materials per delivery = %d"
                    + "\nMinimum amount of products per delivery = %d"
                    + "\nTotal amount of materials = %d"
                    + "\nLog file path = %s\n\n",
                    productCost, materialDeliverySize, batchCapacity,
                    totalMaterial, logFileName).getBytes());
            fos.write(("\nENTREPRE  CUST_0  CUST_1  CUST_2   CRAFT_0 CRAFT_1 CRAFT_2           SHOP"
                    + "                 WORKSHOP").getBytes());
            fos.write(("\n  Stat   Stat BP Stat BP Stat BP   Stat BP Stat BP Stat BP  Stat NCI"
                    + " NPI PCR PMR  APMI NPI NSPM TAPM TNP").getBytes());
        } catch (FileNotFoundException ex) {
            System.exit(1);                                 //change
        } catch (IOException ex) {
            System.exit(1);                                 //change
        }
        logRegister();
    }

    /**
     * Updates the Craftsman state
     *
     * @param craftsID the ID of the Craftsman thread
     * @param newState the state of the Craftsman
     */
    @Override
    public synchronized void updateCraftState(int craftsID, String newState) {
        craftStat[craftsID] = newState;
        logRegister();
    }

    /**
     * Updates the Customer state
     *
     * @param custID the ID of the Customer
     * @param newState the state of the Customer
     */
    @Override
    public synchronized void updateCustState(int custID, String newState) {
        custStat[custID] = newState;
        logRegister();
    }

    /**
     * Updates the Entrepreneur state
     *
     * @param newState the state of the Entrepreneur
     */
    @Override
    public synchronized void updateEntrepState(String newState) {
        entrepreStat = newState;
        logRegister();
    }

    /**
     * Updates the Shop state
     *
     * @param newState the state of the Shop
     */
    @Override
    public synchronized void updateShopState(String newState) {
        shopStat = newState;
        logRegister();
    }

    @Override
    public synchronized void productsRemoved(int nProducts) {
        shopNPI -= nProducts;
    }

    /**
     * The Customer has bought n products
     *
     * @param custID the ID of the Customer
     * @param nProducts the amount of products the Customer has bought
     */
    @Override
    public synchronized void productsBought(int custID, int nProducts) {
        custBP[custID] += nProducts;
    }

    /**
     * A product has been Crafted
     *
     * @param craftsID the ID of the Craftsman who crafted the product
     */
    @Override
    public synchronized void productCrafted(int craftsID) {
        craftPP[craftsID]++;
        workshopNPI++;
        workshopTNP++;
    }

    /**
     * A Craftsman has removed material so he can start the manufacturing
     * process
     */
    @Override
    public synchronized void materialRemoved() {
        workshopAPMI -= productCost;
    }

    /**
     * A call has been made for material delivery to the workshop
     */
    @Override
    public synchronized void callForMaterials() {
        shopPMR = true;
    }

    /**
     * Checks if a call has been made for material delivery to the Workshop
     *
     * @return <ul><li>true, if a call has been made</ul>
     * <ul><li>false, if no call has been made</ul>
     */
    @Override
    public synchronized boolean hasCallForMaterials() {
        return shopPMR;
    }

    /**
     * A call has been made for product delivery to the Shop
     */
    @Override
    public synchronized void callForProducts() {
        shopPCR = true;
    }

    /**
     * Checks if a call has been made for product delivery to the Shop
     *
     * @return true, if a call has been made; false, if no call has been made
     */
    @Override
    public synchronized boolean hasCallForProducts() {
        return shopPCR;
    }

    /**
     * Checks if the supplier has materials left to be delivered
     *
     * @return true, if the supplier still has materials left; false, if the
     * supplier has no materials left to be delivered
     */
    @Override
    public synchronized boolean supplierHasMaterials()
    {
        return totalMaterial >= batchCapacity;
    }
    
    
    
    @Override
    public synchronized int suppliersRealMaterials() {
        int rest = totalMaterial%materialDeliverySize;
        return totalMaterial - rest;
    }

    /**
     * Checks if the Entrepreneur is servicing a Customer
     *
     * @return true, if the Entrepreneur is servicing a Customer false, if the
     * Entrepreneur is not servicing a Customer
     */
    /*
    @Override
    public synchronized boolean isEntrepreServicing() {
        return entrepreStat.equals("ATTENDING_A_CUSTOMER");
    }*/

    /**
     * Checks if the Entrepreneur has any tasks left
     *
     * @return true, if the Entrepreneur still has tasks false, if the
     * Entrepreneur has no tasks left
     */
    @Override
    public synchronized boolean isDaysWorkEnded() {
        return totalMaterial < materialDeliverySize
                && workshopAPMI < productCost
                && shopNPI < 1
                && workshopNPI < batchCapacity;
    }

    /**
     * A customer has entered the Store
     */
    @Override
    public synchronized void customerEnteredStore() {
        shopNCI++;
    }

    /**
     * A customer has left the Store
     */
    @Override
    public synchronized void customerLeftStore() {
        shopNCI--;
    }

    /**
     * A batch of products has been collected from the Workshop
     *
     * @param batchSize the amount of products collected
     */
    @Override
    public synchronized void batchCollected(int batchSize) {
        workshopNPI -= batchSize;
        System.out.println("BATCH COLLECTED"+workshopNPI);
        shopPCR = false;
    }

    /**
     * A batch of products has been delivered to the Shop
     *
     * @param batchSize the amount of products delivered
     */
    @Override
    public synchronized void batchDelivered(int batchSize) {
        shopNPI += batchSize;

    }

    /**
     * The Entrepreneur has collected materials from the supplier
     *
     * @param amount the amount of materials collected
     */
    @Override
    public synchronized void suppliersVisited(int amount) {
        totalMaterial -= amount;
        shopPMR = false;
    }

    /**
     * The Entrepreneur has delivered materials to the Workshop
     *
     * @param amount the amount of materials delivered to the Workshop
     */
    @Override
    public synchronized void materialsDelivered(int amount) {
        workshopAPMI += amount;
        workshopTAPM += amount;
        workshopNSPM++;
    }

    /**
     * Returns the amount of prime materials presently in the Workshop
     *
     * @return the amount of materials
     */
    @Override
    public synchronized int getWorkShopAPMI() {
        return workshopAPMI;
    }

    @Override
    public synchronized boolean isAllproductsSold() {
        System.out.println("1->"+isDaysWorkEnded()+" 2->" + (workshopNPI < batchCapacity) +" 3->"+(shopNPI==0));
        System.out.println("WORKSHOPNPI->"+workshopNPI+" batchCapacity->"+batchCapacity);
        
        return isDaysWorkEnded() && workshopNPI < batchCapacity && shopNPI == 0;
    }

    /* LOGGING */
    /**
     * Register the current problem status to the log file
     */
    private synchronized void logRegister() {

        String string, stringEnterpre, stringCust, stringCraft, stringShop, stringWorkShop;
        stringEnterpre = String.format("  %4s", convertState(entrepreStat));
        stringCust = "";
        for (int i = 0; i < 3; i++) {
            stringCust += String.format("%4s %2d ", convertState(custStat[i]), custBP[i]);
        }
        stringCraft = "";
        for (int i = 0; i < 3; i++) {
            stringCraft += String.format("%4s %2d", convertState(craftStat[i]), craftPP[i]);
        }
        stringShop = String.format("%4s  %2d  %2d   %1s   %1s", shopStat, shopNCI,
                shopNPI, convertBoolToChar(shopPCR), convertBoolToChar(shopPMR));
        stringWorkShop = String.format("  %2d  %2d  %2d   %2d  %2d", workshopAPMI,
                workshopNPI, workshopNSPM, workshopTAPM, workshopTNP);
        string = "\n" + stringEnterpre + "   " + stringCust + "   " + stringCraft + "  "
                + stringShop + "   " + stringWorkShop;
        try {
            fos.write(string.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(MRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Converts a state (String) to a String with the initials only
     *
     * @param state the full String state
     * @return the compacted String state
     */
    private synchronized String convertState(String state) {
        if (state.equals("DEAD")) {
            return "DEAD";
        }

        String returnString = "";
        String[] strings = state.split("_");

        for (String string : strings) {
            if (string.length() > 1) {
                returnString += string.charAt(0);
            }
        }
        return returnString;
    }

    /**
     * Converts a boolean to a char (t/f)
     *
     * @param bool boolean input value
     * @return 'T', if bool == true \n 'F', if bool == false
     */
    private synchronized char convertBoolToChar(boolean bool) {
        if (bool) {
            return 'T';
        } else {
            return 'F';
        }
    }
}
