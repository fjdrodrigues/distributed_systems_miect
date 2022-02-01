/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import monitors.MRepository;
import serverSide.ServerRepository;

/**
 *
 * @author ribeiro
 */
public class RepositoryMain {

    private static int serverPort;
    
    private static int productCost;
    
    private static int materialDeliverySize;
    private static int batchCapacity;
    private static int totalMaterial;
    private static int nCustomers;
    private static int nCraftsman;
    private static String logFileName;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length != 8)
        {
            System.out.println("ERROR: Repository requires 8 arguments:");
            System.exit(1);
        }
        /*
        nCustomers = 3;
        nCraftsman = 3;
        productCost = 1;
        materialDeliverySize = 10;
        batchCapacity = 10;
        totalMaterial = 20;
        logFileName = "logFile1.txt";
        serverPort = 22250
        */
        /*****/
        nCustomers = Integer.parseInt(args[0]);
        nCraftsman = Integer.parseInt(args[1]);
        productCost = Integer.parseInt(args[2]);
        materialDeliverySize = Integer.parseInt(args[3]);
        batchCapacity = Integer.parseInt(args[4]);
        totalMaterial = Integer.parseInt(args[5]);
        logFileName = args[6];
        serverPort = Integer.parseInt(args[7]);  

        MRepository repo = new MRepository(nCustomers, nCraftsman, productCost, materialDeliverySize, batchCapacity,
                totalMaterial, logFileName);
        ServerRepository server = new ServerRepository(serverPort, repo);

        server.start();
        System.out.println("Repository Started!");

    }

}
