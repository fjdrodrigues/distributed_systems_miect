/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import clientSide.ClientCraftsman;
import interfaces.ICraftsmanShop;
import interfaces.ICraftsmanWorkShop;
import threads.TCraftsman;

/**
 *
 * @author ribeiro
 */
public class CraftsmanMain {

    private static String shopIP;

    private static String wsIP;

    private static int clientPortS;

    private static int clientPortW;

    private static int nCraftsman;

    private static int maxSleepTime;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        nCraftsman = 3;
        maxSleepTime = 0;
        shopIP = "127.0.0.1";        //mudar para tabelas
        wsIP = "127.0.0.1";
        clientPortS = 22251;              //mudar
        clientPortW = 22252;              //mudar
        
        Thread.sleep(500);

        
        ClientCraftsman stub = new ClientCraftsman(shopIP, wsIP, clientPortS, clientPortW);

        TCraftsman[] threads = new TCraftsman[nCraftsman];
        for (int i = 0; i < nCraftsman; i++) {
            threads[i] = new TCraftsman((ICraftsmanWorkShop) stub, (ICraftsmanShop) stub, i, 30);
        }
        for (int i = 0; i < nCraftsman; i++) {
            Thread.sleep(500);

            threads[i].start();
            System.out.println("Craftsman " + i + " started!");
        }
    }

}
