package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Manager {

    private TimeCounter timeCounter;
    private HashMap<String, Gate> gates;
    private Planner planner;
    private int threadSignals;

    /**
     * The Manager is the connection between time counter and the Gates threads
     * @param tc Time counter instance
     * @param tollGates Hash Map with all the Gates from the Toll
     */
    public Manager(TimeCounter tc, HashMap<String, Gate> tollGates, Planner planner) {
        this.timeCounter = tc;
        this.gates = tollGates;
        this.threadSignals = 0;
        this.planner = planner;
    }

    /**
     * Starts the time Counter
     * Starts all threads
     */
    public void begin(){
        //Starts time counter Thread
        Thread timerCounter = new Thread(timeCounter);
        timerCounter.start();

        //Starts the gates Threads
        for (Thread thread : gates.values()){
            thread.start();
        }
    }

    /**
     * When all threads have finished their work
     * We tell the timerCounter to increase time
     */
    public synchronized void signal(){
        threadSignals++;
        if (threadSignals == this.gates.size()){
            timeCounter.release();
            threadSignals = 0;
        }

    }
    public void notifyManager(){
        uploadVehiclesInGates();
        releaseGates();
    }
    public void uploadVehiclesInGates(){
        HashMap<Integer, LinkedList<Vehicle>> vehiclesForPriority = planner.getVehiclesForPriority(timeCounter.getTimeCounter());
    }
    /**
     * Tells all threads to start executing
     */
    public void releaseGates(){
        for (Gate gate : gates.values()){
            gate.turnOnGate();
        }
    }






}
