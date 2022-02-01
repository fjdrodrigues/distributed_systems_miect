/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import message.Message;
import monitors.MRepository;
import proxys.TShopRepo;
import proxys.TWSRepo;

/**
 *
 * @author ribeiro
 */
public class ServerRepository extends Thread {

    private final MRepository mRepo;
    private ServerCom comServer = null;

    public ServerRepository(int portNumber, MRepository mRepo) {
        this.mRepo = mRepo;
        comServer = new ServerCom(portNumber);
    }

    @Override
    public void run() {
        ServerCom uServerCom;
        //starts the main running course
        comServer.start();
        while (true) {
            uServerCom = comServer.accept();
            Message received =(Message) uServerCom.readObject();
            //System.out.println("RECEIVED:\n" + received.toString());
            Thread thread;
            if (received.getSenderType().equals("shop")) {
                thread = new TShopRepo(uServerCom, received, mRepo);
            } else {
                thread = new TWSRepo(uServerCom, received, mRepo);
            }

            thread.start();
        }
    }

}
