package serverSide;

import interfaces.ICraftsmanShop;
import interfaces.ICustomer;
import interfaces.IEntrepreShop;
import message.Message;
import monitors.MShop;
import proxys.TCraftsmanShop;
import proxys.TCustomerShop;
import proxys.TEntrepreneurShop;
/**
 *
 * @author ribeiro
 */
public class ServerShop extends Thread {

    private final MShop mShop;
    private ServerCom comServer = null;

    public ServerShop(int portNumber, MShop mShop) {
        this.mShop = mShop;

        comServer = new ServerCom(portNumber);
    }

    @Override
    public void run() {
        ServerCom uServerCom;
        comServer.start();
        while (true) {
            uServerCom = comServer.accept();
            Message received =(Message) uServerCom.readObject();

            Thread thread;
            switch (received.getSenderType()) {
                default:
                    thread = null;
                    break;
                case "customer":
                    thread = new TCustomerShop(uServerCom, received,(ICustomer) mShop);
                    break;
                case "entrepreneur":
                    thread = new TEntrepreneurShop(uServerCom, received,(IEntrepreShop) mShop);
                    break;
                case "craftsman":
                    thread = new TCraftsmanShop(uServerCom, received,(ICraftsmanShop) mShop);
                    break;
            }
            if (thread != null) {
                thread.start();
            }
        }
    }

}
