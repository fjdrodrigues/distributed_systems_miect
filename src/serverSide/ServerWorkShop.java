package serverSide;

import interfaces.ICraftsmanWorkShop;
import interfaces.IEntrepreWorkShop;
import message.Message;
import monitors.MWorkShop;
import proxys.TCraftsmanWS;
import proxys.TEntrepreneurWS;

/**
 *
 * @author João
 */
public class ServerWorkShop extends Thread {

    private final MWorkShop mWorkShop;
    private ServerCom comServer = null;

    public ServerWorkShop(int portNumber, MWorkShop mWorkShop) {
        this.mWorkShop = mWorkShop;

        comServer = new ServerCom(portNumber);
    }

    @Override
    public void run() {
        ServerCom uServerCom;
        comServer.start();
        while (true) {
            uServerCom = comServer.accept();
            Message received = (Message) uServerCom.readObject();
            //System.out.println("RECEIVED:\n" + received.toString());
            Thread thread;
            if (received.getSenderType().equals("entrepreneur")) {
                thread = new TEntrepreneurWS(uServerCom, received, (IEntrepreWorkShop) mWorkShop);
            } else {
                thread = new TCraftsmanWS(uServerCom, received, (ICraftsmanWorkShop) mWorkShop);
            }
            thread.start();
        }
    }

}
