/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import clientSide.ClientWorkShop;
import interfaces.IWorkShop;
import monitors.MWorkShop;
import serverSide.ServerWorkShop;

/**
 *
 * @author ribeiro
 */
public class WSMain {

    private static int nCraftsman;

    private static int productCost;

    private static int materialDeliverySize;

    private static int batchCapacity;

    private static String repoIP;

    private static int clientPort;

    private static int serverPort;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        nCraftsman = 3;
        productCost = 1;
        materialDeliverySize = 10;
        batchCapacity = 10;

        repoIP = "127.0.0.1";        //mudar para tabelas
        clientPort = 22250;              //mudar
        serverPort = 22252;

        ClientWorkShop clientWorkShop = new ClientWorkShop(repoIP, clientPort);

        //estatico agora mas mudar
        MWorkShop mWorkShop = new MWorkShop((IWorkShop) clientWorkShop, nCraftsman, productCost, materialDeliverySize, batchCapacity);

        ServerWorkShop stub = new ServerWorkShop(serverPort, mWorkShop);

        stub.start();

        System.out.println("Workshop started!");

    }

}
