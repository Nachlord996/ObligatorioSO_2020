package org.ucu.fit.so;

import java.util.concurrent.Semaphore;

public class TimeCounter implements Runnable{

    public Semaphore semaphore;
    private int timeCounter;
    private int THREADS_NUMBER;

    public TimeCounter(int threadsNumber){
        if (threadsNumber < 0){
            throw new IllegalArgumentException("Number of threads can't be negative");
        }
        timeCounter = 0;
        restore(0);
        THREADS_NUMBER = threadsNumber;
    }

    public void restore(){
        semaphore = new Semaphore((-1)*THREADS_NUMBER, true);
    }

    private void restore(int permits){
        semaphore = new Semaphore(permits, true);
    }

    @Override
    public void run() {
        System.out.println("El reloj ha sido iniciado");
        while(true){
            try {
                System.out.println("t = " + timeCounter);
                restore();
                if (timeCounter == 0) {
                    Program.PROCESS_MANAGER.startTicking();
                }
                semaphore.acquire();
                timeCounter++;

                if (timeCounter > 0){
                    Program.PROCESS_MANAGER.clockTick();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
