package org.ucu.fit.so;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class Manager {

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
     * The Manager is the connection between time counter and the Gates threads
     * @param tollGates Hash Map with all the Gates from the Toll
     * @param planner The planer selects the cars that are able to enter toolGates
     * @param outputPath Path to write the logs
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


    public void uploadVehiclesInGatesWithPriorities(){
        //Current time in simulation
        int time = timeCounter.getActualTime();

        //Gets HashMap with new vehicles.
        LinkedList<Vehicle>[] newVehicles = planner.getVehiclesForPriority(time);

        //Available gates are filled with cars
        LinkedList<TollGate> availableGates = getAvailableGates();
        for (TollGate tollGate : availableGates){
            for (LinkedList<Vehicle> vehicles : newVehicles) {
                if (vehicles != null && !vehicles.isEmpty()) {
                    tollGate.addVehicleToRoad(vehicles.pop());
                    break;
                }
            }
        }
    }

    public void returnVehiclesToPlanner(LinkedList<Vehicle> vehicles){
        int time = timeCounter.getActualTime();
        for(Vehicle vehicle : vehicles){
            planner.returnVehicle(vehicle);
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
        availableGates.sort(new Comparator<TollGate>() {
            @Override
            public int compare(TollGate o1, TollGate o2) {
                Integer usedCapacity1 = o1.getUsedCapacity();
                Integer usedCapacity2 = o2.getUsedCapacity();
                return usedCapacity1.compareTo(usedCapacity2);
            }
        });
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
    public synchronized void reportTask(TaskReport report) {
        report.setInstant(timeCounter.getActualTime());
        LinkedList<String> lines = report.getReportLines();
        for (String logLine : lines) {
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
