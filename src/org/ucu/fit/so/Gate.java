package org.ucu.fit.so;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public abstract class Gate extends Thread {

    protected String uuid;
    protected Semaphore uniquenessSemaphore;

    Gate(int gateNumber){
        this.uuid = "G" + gateNumber;
        uniquenessSemaphore = new Semaphore(0);
    }

    @Override
    public void run(){
        int i = 20;
        while(i > 0){
            try {
                //If timer allows
                uniquenessSemaphore.acquire();

                TaskReport report = this.consume();

                //Report done task
                Program.PROCESS_MANAGER.reportTask(report);

                //Tells the manager that ended
                Program.PROCESS_MANAGER.signal();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;
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
