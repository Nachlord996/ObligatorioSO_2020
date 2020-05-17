package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Planner {
    /**
     * HashMap with type of vehicle on keys
     * The value is the priority number of the type of vehicle
     */
    private HashMap<String,Integer> vehiclesPriorities;

    /**
     * This HashMap has the time the vehicle enters the toll as keys
     * The value is a linked list of vehicles that appear in the selected time
     */
    private HashMap<Integer, LinkedList<Vehicle>> vehicles;

    /**
     * The planner manages the vehicles by time. Its work is to select vehicles to put in gates
     * @param vehiclesPriorities Key = vehicleType (string). Value = priority value (integer)
     * @param vehiclesForTime Key = timeToAppear (integer). Value = Vehicles linked list
     */
    public Planner(HashMap<String, Integer> vehiclesPriorities, HashMap<Integer, LinkedList<Vehicle>> vehiclesForTime) {
        this.vehiclesPriorities = vehiclesPriorities;
        this.vehicles = vehiclesForTime;
    }

    /**
     * Returns a HashMap with: key = time to appear. value = linked list of vehicles that appear in that time
     * @param time Time to get the vehicles
     * @return HashMap with: key = time to appear. value = linked list of vehicles that appear in that time
     */
    public HashMap<Integer, LinkedList<Vehicle>> getVehiclesForPriority(int time) {
        HashMap<Integer,LinkedList<Vehicle>> vehiclesForPriority = new HashMap<>();
        if (vehicles.containsKey(time)){
            for(Vehicle vehicle: vehicles.get(time)){
                int priority = vehiclesPriorities.get(vehicle.getTypeOfVehicle());
                if(!vehiclesForPriority.containsKey(priority)){
                    vehiclesForPriority.put(priority,new LinkedList<>());
                }
                vehiclesForPriority.get(priority).add(vehicle);
            }
        }
        return vehiclesForPriority;
    }

    /**
     * Returns a list with vehicles of a required time
     * @param time time of arrival of the vehicles
     * @return Linked list with vehicles
     */
    public LinkedList<Vehicle> getVehicleArrivedAtTime(int time){
        LinkedList<Vehicle> list = vehicles.remove(time);
        return list;
    }

    public boolean isEmpty(){
        return vehicles.isEmpty();
    }
}
