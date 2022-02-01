/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import clientSide.ClientCustomer;
import interfaces.ICustomer;
import threads.TCustomer;

/**
 *
 * @author ribeiro
 */
public class CustomerMain {

    private static String shopIP;

    private static int clientPort;

    private static int nCustomers;

    private static int maxSleepTime;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        nCustomers = 3;
        maxSleepTime = 0;
        shopIP = "127.0.0.1";        //mudar para tabelas
        clientPort = 22251;              //mudar


        ClientCustomer stub = new ClientCustomer(shopIP, clientPort);

        TCustomer[] threads = new TCustomer[nCustomers];
        for (int i = 0; i < nCustomers; i++) {
            threads[i] = new TCustomer((ICustomer) stub, i, maxSleepTime);
        }

        for (int i = 0; i < nCustomers; i++) {
            threads[i].start();
            System.out.println("Customer " + i + " started!");
        }
    }

}
