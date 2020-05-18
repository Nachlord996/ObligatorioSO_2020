package org.ucu.fit.so;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public abstract class Gate extends Thread {

    /**
     * Unique ID of the gate
     */
    protected String uuid;

    /**
     * This Semaphore allows the gate to execute once per time unit
     * The TimeCounter makes the Manager release it
     */
    protected Semaphore uniquenessSemaphore;

    /**
     * The gates takes care of the movement and payment of cars
     * Each Gate is a Thread
     * @param gateNumber Number of thread to create id. Each number shoud be unique
     */
    Gate(int gateNumber){
        this.uuid = "G" + gateNumber;
        uniquenessSemaphore = new Semaphore(0);
    }

    @Override
    public void run(){
        while(true){
            try {
                if(Program.PROCESS_MANAGER.hasEnded()){
                    break;
                }
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
