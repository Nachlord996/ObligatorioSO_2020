package org.ucu.fit.so;

import java.util.concurrent.Semaphore;

public abstract class Gate extends Thread {

    protected String uuid;
    protected Semaphore uniquenessSemaphore;
    protected Manager manager;

    Gate(int gateNumber){
        this.uuid = "G" + gateNumber;
        uniquenessSemaphore = new Semaphore(0);
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    @Override
    public void run(){
        while(true){
            try {
                if(manager.hasEnded()){
                    break;
                }
                //If timer allows
                uniquenessSemaphore.acquire();

                TaskReport report = this.consume();

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
    public void turnOnGate(){
        uniquenessSemaphore.release();
    }

    abstract TaskReport consume();
}
