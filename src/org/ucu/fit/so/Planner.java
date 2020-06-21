package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;

public class Planner {
    /**
     * HashMap with type of vehicle on keys
     * The value is the priority number of the type of vehicle
     */
    private final HashMap<String, Integer> vehiclesPriorities;

    /**
     * This HashMap has the time the vehicle enters the toll as keys
     * The value is a linked list of vehicles that appear in the selected time
     */
    private final HashMap<Integer, LinkedList<Vehicle>> vehiclesForTime;
    private final LinkedList<Vehicle>[] prospectsToEnterWithPriority;

    /**
     * The planner manages the vehicles by time. Its work is to select vehicles to put in gates
     *
     * @param vehiclesPriorities Key = vehicleType (string). Value = priority value (integer)
     * @param vehiclesForTime    Key = timeToAppear (integer). Value = Vehicles linked list
     */
    public Planner(HashMap<String, Integer> vehiclesPriorities, HashMap<Integer, LinkedList<Vehicle>> vehiclesForTime, int amountOfPriorities) {
        this.vehiclesPriorities = vehiclesPriorities;
        this.vehiclesForTime = vehiclesForTime;
        this.prospectsToEnterWithPriority = new LinkedList[amountOfPriorities];
    }

    /**
     * Returns a HashMap with: key = priority to appear. value = linked list of vehicles that appear in that time
     *
     * @param time Time to get the vehicles
     * @return HashMap with: key = priority to appear. value = linked list of vehicles that appear in that time
     */
    public LinkedList<Vehicle>[] getVehiclesForPriority(int time) {
        HashMap<Integer, LinkedList<Vehicle>> vehiclesForPriority = new HashMap<>();
        if (vehiclesForTime.containsKey(time)) {
            for (Vehicle vehicle : getVehicleArrivedAtTime(time)) {
                int priority = vehiclesPriorities.get(vehicle.getTypeOfVehicle());
                if (!vehiclesForPriority.containsKey(priority)) {
                    vehiclesForPriority.put(priority, new LinkedList<>());
                }
                vehiclesForPriority.get(priority).add(vehicle);
            }
        }
        if (vehiclesForPriority != null) {
            for (Integer key : vehiclesForPriority.keySet()) {
                for (Vehicle vehicle : vehiclesForPriority.get(key)) {
                    if (prospectsToEnterWithPriority[key - 1] == null) {
                        prospectsToEnterWithPriority[key - 1] = new LinkedList<>();
                    }
                    prospectsToEnterWithPriority[key - 1].addLast(vehicle);
                }
            }
        }

        return prospectsToEnterWithPriority;
    }


    /**
     * Returns a list with vehicles of a required time
     *
     * @param time time of arrival of the vehicles
     * @return Linked list with vehicles
     */
    public LinkedList<Vehicle> getVehicleArrivedAtTime(int time) {
        LinkedList<Vehicle> list = null;
        if (vehiclesForTime != null) {
            list = vehiclesForTime.remove(time);
        }
        return list;
    }

    public boolean isEmpty() {
        boolean endIsHere = vehiclesForTime.isEmpty();
        if (endIsHere){
            for (LinkedList<Vehicle> vehicles: prospectsToEnterWithPriority){
                if (vehicles!=null && !vehicles.isEmpty()){
                    endIsHere = false;
                    break;
                }
            }
        }
        return endIsHere;
    }

    public void returnVehicle(Vehicle vehicle) {
        if (vehicle.getPriority() != 1) {
            vehicle.setPriority(2);
        }
        if (prospectsToEnterWithPriority[vehicle.getPriority()-1] == null) {
            prospectsToEnterWithPriority[vehicle.getPriority()-1] = new LinkedList<>();
        }
        prospectsToEnterWithPriority[vehicle.getPriority()-1].addFirst(vehicle);
    }
}
