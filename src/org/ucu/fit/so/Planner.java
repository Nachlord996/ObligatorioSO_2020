package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Planner {
    private HashMap<String,Integer> vehiclesPriorities;
    private HashMap<Integer, LinkedList<Vehicle>> vehicles;
    private TimeCounter timeCounter;

    public Planner(HashMap<String, Integer> vehiclesPriorities, HashMap<Integer, LinkedList<Vehicle>> vehiclesForTime, TimeCounter timeCounter) {
        this.vehiclesPriorities = vehiclesPriorities;
        this.vehicles = vehiclesForTime;
        this.timeCounter = timeCounter;
    }

    public HashMap<Integer, LinkedList<Vehicle>> getVehiclesForPriority(int time) {
        HashMap<Integer,LinkedList<Vehicle>> vehiclesForPriority = new HashMap<>();
        for(Vehicle vehicle: vehicles.get(time)){
            int priority = vehiclesPriorities.get(vehicle.getTypeOfVehicle());
            if(!vehiclesForPriority.containsKey(priority)){
                vehiclesForPriority.put(priority,new LinkedList<>());
            }
            vehiclesForPriority.get(priority).add(vehicle);
        }
        return vehiclesForPriority;
    }
}
