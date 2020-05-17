package org.ucu.fit.so;

import java.util.concurrent.Semaphore;

public class TimeCounter implements Runnable{

    //Controls that all gates have executed
    private Semaphore timerSemaphore;

    private int timeCounter;
    private int THREADS_NUMBER;

    public TimeCounter(){
        timerSemaphore = new Semaphore(1);
        timeCounter = 0;
    }

    public int getTimeCounter() {
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
        System.out.println("El reloj ha sido iniciado");
        while(true){ //Mientras hayan autos en la cola
            try {
                //If threads have stopped working
                this.timerSemaphore.acquire();

                //Critical section
                System.out.println("t = " + timeCounter);
                timeCounter++;

                //Turns on all the unity Semaphores from the Threads
                Program.PROCESS_MANAGER.notifyManager();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
