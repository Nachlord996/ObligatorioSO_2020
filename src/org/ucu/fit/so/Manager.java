package org.ucu.fit.so;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Manager {

    private TimeCounter timeCounter;
    private HashMap<String, Gate> gates;
    private Semaphore semaphore = new Semaphore(0,true);
    private boolean islastThread;

    public Manager(TimeCounter tc, HashMap<String, Gate> tollGates) {
        this.timeCounter = tc;
        this.gates = tollGates;
    }

    public void begin(){
        Thread timerCounter = new Thread(timeCounter);
        timerCounter.start();

        for (Thread thread : gates.values()){
            thread.start();
        }
    }

    public void takeTurn(String uuid){
        try {
            Gate gate = gates.get(uuid);
            if (gate != null){
                while (gate.hasWorked){
                }
                semaphore.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reportFinish(String uuid){
        Gate gate = gates.get(uuid);
        if (gate != null){
            semaphore.release();
            gate.setHasWorked(true);
        }
    }

    public synchronized void signal() throws InterruptedException {
        if (timeCounter.semaphore.availablePermits() == -1) {
            islastThread = true;
            timeCounter.semaphore.release();
        }
        timeCounter.semaphore.release();
        while (islastThread){ }
    }

    public void clockTick(){
        for (String key : gates.keySet()){
            gates.get(key).setHasWorked(false);
        }
        System.out.println("Los permits del semaforo son: "  +semaphore.availablePermits());
        islastThread = false;
    }

    public void startTicking(){
        semaphore.release();
    }


}
