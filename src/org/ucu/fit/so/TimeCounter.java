package org.ucu.fit.so;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.util.concurrent.Semaphore;

public class TimeCounter implements Runnable{

    //Controls that all gates have executed
    private Semaphore timerSemaphore;

    /**
     * Int represents the moment of time of the simulation
     */
    private int timeCounter;

    /**
     * Class that simulates time
     */
    public TimeCounter(){
        timerSemaphore = new Semaphore(1);
        timeCounter = -1;
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
        System.out.println("El reloj ha sido iniciado");
        while(true){ //Mientras hayan autos en la cola
            try {

                //If threads have stopped working
                this.timerSemaphore.acquire();

                if(Program.PROCESS_MANAGER.hasEnded()) {
                    break;
                }

                timeCounter++;
                System.out.println("t = " + timeCounter);

                //Turns on all the unity Semaphores from the Threads
                Program.PROCESS_MANAGER.notifyManager();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
