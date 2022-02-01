package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Register line module by implementation of ReentrantLock
 *
 * @author ribeiro
 */
public class RegisterLine {

    /**
     * ReentrantLock object
     */
    private final Lock lock;

    private final Condition conditionEntrepreneur;
    private final Condition conditionCustomers;

    private boolean customerPaying;
    private boolean entrepreneurServicing;

    private int count;

    /**
     * Constructor for RegisterLine, initializes the customer queue
     */
    public RegisterLine() {
        lock = new ReentrantLock(true);
        conditionCustomers = lock.newCondition();
        conditionEntrepreneur = lock.newCondition();
        this.customerPaying = false;
        this.entrepreneurServicing = false;
        count = 0;
    }

    /**
     * A customer enters the line and his thread is paused
     *
     */
    public void enterLine() {
        count++;
        lock.lock();
        try {

            while (!entrepreneurServicing) {

                try {
                    conditionCustomers.await();
                } catch (InterruptedException ex) {
                }
            }
            entrepreneurServicing = false;
            customerPaying = true;

            count--;
            conditionEntrepreneur.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * removes the first element to enter the line (currently)
     */
    public void removeFirstInLine() {
        lock.lock();
        try {
            entrepreneurServicing = true;
            conditionCustomers.signal();

            while (!customerPaying) {
                try {
                    conditionEntrepreneur.await();
                } catch (InterruptedException ex) {
                }
            }
            customerPaying = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return count == 0;
    }

}
