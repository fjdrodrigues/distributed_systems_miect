/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import clientSide.ClientEntrepreneur;
import interfaces.IEntrepreShop;
import interfaces.IEntrepreWorkShop;
import threads.TEntrepre;

/**
 *
 * @author ribeiro
 */
public class EntrepreneurMain {

    private static String shopIP;

    private static String wsIP;

    private static int shopPort;

    private static int workShopPort;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        shopIP = "127.0.0.1";        //mudar para tabelas
        wsIP = "127.0.0.1";
        shopPort = 22251;              //mudar
        workShopPort = 22252;

        int maxSleepTime = 0;

        ClientEntrepreneur stub = new ClientEntrepreneur(shopIP, wsIP, shopPort, workShopPort);

        TEntrepre thread = new TEntrepre((IEntrepreShop) stub, (IEntrepreWorkShop) stub, maxSleepTime);
        thread.start();

        System.out.println("Entrepreneur started!");

    }
}
