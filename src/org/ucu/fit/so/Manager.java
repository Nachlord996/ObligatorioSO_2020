package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Manager {

    /**
     * This class simulates time
     */
    private TimeCounter timeCounter;

    /**
     * A HashMap containing <Gate id, TollGate> </Gate>
     */
    private HashMap<String, TollGate> gates;

    /**
     * TO BE UPDATED
     * The planner retrieves the vehicle that should be placed in the TollGates
     * In further versions processes planing is going to be implemented
     */
    private Planner planner;

    /**
     * Number of threads that have ended their work in a unit of time
     */
    private int threadSignals;


    private LogHandler logger;

    /**
     * Thread to execute the time counter
     */
    private Thread timerCounter;


    /**
     * TO BE UPDATED
     * List of vehicles that are able to enter a tollGate
     */
    private LinkedList<Vehicle> prospectsToEnter;

    /**
     * The Manager is the connection between time counter and the Gates threads
     * @param tc Time counter instance
     * @param tollGates Hash Map with all the Gates from the Toll
     * @param planner The planer selects the cars that are able to enter toolGates
     * @param archive
     */
    public Manager(TimeCounter tc, HashMap<String, TollGate> tollGates, Planner planner, LogArchive archive) {
        this.timeCounter = tc;
        this.gates = tollGates;
        this.threadSignals = 0;
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
        timerCounter = new Thread(timeCounter);
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

    /**
     * This method is executed when the timeCounter
     */
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
                if (!prospectsToEnter.isEmpty()){
                    tollGate.addVehicleToRoad(prospectsToEnter.pop());
                }

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
            if(!gate.roadIsFull()) {
                availableGates.add(gate);
            }
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

    /**
     * Ads a task to the log
     * @param report
     */
    public synchronized void reportTask(TaskReport report){
        report.setInstant(timeCounter.getActualTime());
        LinkedList<String> lines = report.getReportLines();
        for(String logLine : lines){
            logger.log(logLine);
        }
    }

    /**
     * Returns true if:
     * There is no more vehicles waiting to access the toll
     * All gates are empty and there is no vehicles on them
     * These are the conditions that make the program end
     * @return true if the program can be ended
     */
    public boolean hasEnded(){
        boolean endIsHere = planner.isEmpty();
        if (endIsHere){
            for (TollGate gate : gates.values()){
                if (!gate.roadIsEmpty()){
                    endIsHere = false;
                    break;
                }
            }
        }
        return endIsHere;
    }

    /**
     * Returns true if there is a gate running
     * @return true if there is a gate running
     */
    public boolean stillRunning(){
        boolean stillRunning = false;
        for (Gate gate : gates.values()){
            stillRunning = stillRunning || gate.isAlive();
        }
        stillRunning = stillRunning || timerCounter.isAlive();
        return stillRunning;
    }
}
