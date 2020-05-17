package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Manager {

    private TimeCounter timeCounter;
    private HashMap<String, TollGate> gates;
    private Planner planner;

    private int threadSignals;

    private LogArchive archive;
    private LogHandler logger;

    private LinkedList<Vehicle> prospectsToEnter;

    /**
     * The Manager is the connection between time counter and the Gates threads
     * @param tc Time counter instance
     * @param tollGates Hash Map with all the Gates from the Toll
     */
    public Manager(TimeCounter tc, HashMap<String, TollGate> tollGates, Planner planner) {
        this.timeCounter = tc;
        this.gates = tollGates;
        this.threadSignals = 0;
        this.archive = new LogArchive();
        this.logger = new LogHandler(archive);
        this.planner = planner;
        this.prospectsToEnter = new LinkedList<Vehicle>();
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

    /**
     * The method we use here to put cars into the toll is JUST TO TEST THIS VERSION. It will be updated soon to
     * implement multi queue planning.
     * This method follows these Steps:
     * THIS IS JUST FOR THIS VERSION Obtain List with vehicles that arrived on the current time
     * and wants to enter de toll.
     * THIS IS JUST FOR THIS VERSION: add them to a linkedList
     * Get gates with room to add a vehicle (at the beginning of the road obviously)
     * To each available gate send a vehicle
     */
    public void uploadVehiclesInGates(){
        //Current time in simulation
        int time = timeCounter.getActualTime();

        //Gets List with new vehicles. TO BE UPDATED SOON
        LinkedList<Vehicle> newVehicles = planner.getVehicleArrivedAtTime(time);

        //Ads new vehicles to a linked list so they can wait. TO BE UPDATED SOON
        if (newVehicles != null){
            for(Vehicle vehicle : newVehicles){
                prospectsToEnter.addLast(vehicle);
            }
        }

        //Available gates are filled with cars
        LinkedList<TollGate> availableGates = getAvailableGates();
        if (availableGates != null){
            for (TollGate tollGate : availableGates){
                tollGate.addVehicleToRoad(prospectsToEnter.pop());
            }
        }
    }

    /**
     * Return a list with gates that are available to add vehicles
     * O(Gates)
     * @return LinkedList<TollGate> </TollGate>
     */
    private LinkedList<TollGate> getAvailableGates(){
        LinkedList<TollGate> availableGates = new LinkedList<>();
        for (TollGate gate : gates.values()){
            availableGates.add(gate);
        }
        return availableGates;
    }


    /**
     * Tells all threads to start executing
     */
    public void releaseGates(){
        for (Gate gate : gates.values()){
            gate.turnOnGate();
        }
    }

    public synchronized void reportTask(TaskReport report){
        report.setInstant(timeCounter.getActualTime());
        LinkedList<String> lines = report.getReportLines();
        for(String logLine : lines){
            logger.log(logLine);
        }
    }
}
