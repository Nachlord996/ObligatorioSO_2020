package org.ucu.fit.so;

import java.util.concurrent.Semaphore;

public abstract class Gate extends Thread {

    /**
     * Unique ID of the gate
     */
    protected final String uuid;


    /**
     * This Semaphore allows the gate to execute once per time unit
     * The TimeCounter makes the Manager release it
     */
    protected final Semaphore uniquenessSemaphore;
    protected Manager manager;

    /**
     * The gates takes care of the movement and payment of cars
     * Each Gate is a Thread
     *
     * @param gateNumber Number of thread to create id. Each number should be unique
     */
    Gate(int gateNumber) {
        this.uuid = "G" + gateNumber;
        uniquenessSemaphore = new Semaphore(0);
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        TaskReport report;
        while (true) {
            try {
                //If timer allows
                uniquenessSemaphore.acquire();

                if (manager.hasEnded()) {
                    manager.signal();
                    break;
                }

                report = this.consume();

                //Report done task
                manager.reportTask(report);

                //Tells the manager that ended
                manager.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Turns on the uniquenessSemaphore
     * This causes that the thread can run one time
     */
    public void turnOnGate() {
        uniquenessSemaphore.release();
    }

    abstract TaskReport consume();
}
