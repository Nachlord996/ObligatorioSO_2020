package org.ucu.fit.so;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Manager<prospectsToEnter2> {

    /**
     * This class simulates time
     */
    private final TimeCounter timeCounter;
    /**
     * A HashMap containing <Gate id, TollGate> </Gate>
     */
    private final HashMap<String, TollGate> gates;
    /**
     * TO BE UPDATED
     * The planner retrieves the vehicle that should be placed in the TollGates
     * In further versions processes planing is going to be implemented
     */
    private final Planner planner;

    private final String outputPath;
    /**
     * Number of threads that have ended their work in a unit of time
     */
    private int threadSignals = 0;


    private final LogHandler logger = new LogHandler();
    /**
     * TO BE UPDATED
     * List of vehicles that are able to enter a tollGate
     */
    private final LinkedList<Vehicle> prospectsToEnter = new LinkedList<>();

    private final LinkedList<Vehicle>[] prospectsToEnterWithPriority;


    /**
     * The Manager is the connection between time counter and the Gates threads
     * @param tollGates Hash Map with all the Gates from the Toll
     * @param planner The planer selects the cars that are able to enter toolGates
     * @param outputPath Path to write the logs
     */
    public Manager(HashMap<String, TollGate> tollGates, Planner planner, String outputPath,int amountOfPriorities) {
        this.timeCounter = new TimeCounter(this);
        this.gates = tollGates;
        this.planner = planner;
        this.outputPath = outputPath;
        this.prospectsToEnterWithPriority = new LinkedList[amountOfPriorities];
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

    /**
     * This method is executed when the timeCounter
     */
    public void notifyManager(){
        uploadVehiclesInGatesWithPriorities();
        releaseGates();
    }

    public void makeLogReport(){
        Writer.write(outputPath, logger.getLogfile());
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

    public void uploadVehiclesInGatesWithPriorities(){
        //Current time in simulation
        int time = timeCounter.getActualTime();

        //Gets HashMap with new vehicles.
        HashMap<Integer,LinkedList<Vehicle>> newVehicles = planner.getVehiclesForPriority(time);


        //Ads new vehicles to a linked list so they can wait.
        if (newVehicles != null){
            for(Integer key : newVehicles.keySet()){
                for(Vehicle vehicle:newVehicles.get(key)){
                    if(prospectsToEnterWithPriority[key-1] == null){
                        prospectsToEnterWithPriority[key-1] = new LinkedList<>();
                    }
                    prospectsToEnterWithPriority[key-1].addLast(vehicle);
                }
            }
        }

        //Available gates are filled with cars
        LinkedList<TollGate> availableGates = getAvailableGates();
        for (TollGate tollGate : availableGates){
            for (LinkedList<Vehicle> vehicles : prospectsToEnterWithPriority) {
                if (vehicles != null && !vehicles.isEmpty()) {
                    tollGate.addVehicleToRoad(vehicles.pop());
                    break;
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
            if(gate.roadIsNotFull()) {
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
     * @param report report to add to log file
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

}
