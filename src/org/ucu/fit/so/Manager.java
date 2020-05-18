package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Manager {

    private final TimeCounter timeCounter;
    private final HashMap<String, TollGate> gates;
    private final Planner planner;
    private final String outputPath;
    private int threadSignals = 0;
    private final LogHandler logger = new LogHandler();
    private final LinkedList<Vehicle> prospectsToEnter = new LinkedList<>();

    /**
     * The Manager is the connection between time counter and the Gates threads
     * @param outputPath Path to write the logs
     * @param tollGates Hash Map with all the Gates from the Toll
     */
    public Manager(HashMap<String, TollGate> tollGates, Planner planner, String outputPath) {
        this.timeCounter = new TimeCounter(this);
        this.gates = tollGates;
        this.planner = planner;
        this.outputPath = outputPath;
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
        for (Gate gate : gates.values()){
            gate.setManager(this);
            gate.start();
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

    public void makeLogReport(){
        Writer.write(outputPath, logger.getLogfile().getLogMessage());
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
        for (TollGate tollGate : availableGates){
            if (!prospectsToEnter.isEmpty()){
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

    public synchronized void reportTask(TaskReport report){
        report.setInstant(timeCounter.getActualTime());
        LinkedList<String> lines = report.getReportLines();
        for(String logLine : lines){
            logger.log(logLine);
        }
    }

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

    /*public boolean stillRunning(){
        boolean stillRunning = false;
        for (Gate gate : gates.values()){
            stillRunning = stillRunning || gate.isAlive();
        }
        stillRunning = stillRunning || timerCounter.isAlive();
        return stillRunning;
    }*/
}
