package org.ucu.fit.so;

import java.util.concurrent.Semaphore;

public class TimeCounter implements Runnable{

    //Controls that all gates have executed

    private final Semaphore timerSemaphore;
    private final Manager manager;
    /**
     * Int represents the moment of time of the simulation
     */
    private int timeCounter;
    /**
     * Class that simulates time
     */
    public TimeCounter(Manager manager){
        this.timerSemaphore = new Semaphore(1);
        this.timeCounter = -1;
        this.manager = manager;

    }

    /**
     * Returns the moment of time during the simulation
     * @return int, time moment
     */
    public int getActualTime(){
        return timeCounter;
    }

    /**
     * Allows the timeCounter to enter the critical section
     */
    public void release(){
        this.timerSemaphore.release();
    }

    @Override
    public void run() {
        System.out.println("The clock has started");
        while(true){
            try {
                //If threads have stopped working the clock can run
                this.timerSemaphore.acquire();

                if(manager.hasEnded()) {
                    manager.releaseGates();
                    break;
                }

                timeCounter++;
                System.out.println("t = " + timeCounter);

                //Turns on all the unity Semaphores from the Threads
                manager.notifyManager();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The clock has finished");
        manager.makeLogReport();
    }
}
