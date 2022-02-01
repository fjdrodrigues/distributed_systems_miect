package main;

import clientSide.ClientShop;
import interfaces.IShop;
import monitors.MShop;
import monitors.RegisterLine;
import serverSide.ServerShop;

/**
 *
 * @author ribeiro
 */
public class ShopMain {

    private static int nCustomers;

    private static int maxPurchases;

    private static String repoIP;

    private static int clientPort;

    private static int serverPort;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        /*
        nCustomers = 3;
        maxPurchases = 2;
        repoIP = "127.0.0.1";        //mudar para tabelas
        clientPort = 22250;              //mudar
        serverPort = 22251;*/
        
        if(args.length != 5)
        {
            System.out.println("ERROR: Shop requires 5 arguments to start!");
            System.exit(1);
        }
        nCustomers = Integer.parseInt(args[0]);
        maxPurchases = Integer.parseInt(args[1]);
        repoIP = args[2];
        clientPort = Integer.parseInt(args[3]);
        serverPort = Integer.parseInt(args[4]);
        
        RegisterLine registerLine = new RegisterLine();

        ClientShop clientShop = new ClientShop(repoIP, clientPort);

        MShop mShop = new MShop((IShop) clientShop, registerLine, nCustomers, maxPurchases);       //estatico agora mas mudar

        ServerShop stub = new ServerShop(serverPort, mShop);

        stub.start();

        System.out.println("Shop started!");

    }

}
