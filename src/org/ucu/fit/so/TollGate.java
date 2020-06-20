package org.ucu.fit.so;

import java.util.LinkedList;

public class TollGate extends Gate {

    private final Vehicle[] road;
    private final int timeToCharge;
    private int timeLeftToCharge;
    private int usedCapacity = 0;

    public int getUsedCapacity() {
        return usedCapacity;
    }

    /**
     * Class representing a tollGate
     *
     * @param gateNumber Number of gate to generate unique id
     * @param roadSize   Size of the road of the gate
     * @param timeToPay  Time that a vehicle spends passing through the gate
     */
    TollGate(int gateNumber, int roadSize, int timeToPay) {
        super(gateNumber);
        this.road = new Vehicle[roadSize];
        this.timeToCharge = timeToPay;
        this.timeLeftToCharge = timeToPay;
    }

    @Override
    TaskReport consume() {
        TaskReport report = new TaskReport();
        Task task;
        if (!isWorking()) {
            task = new Task(this.uuid, "Casilla fuera de funcionamiento");
            report.addTask(task);
            return report;
        }
        //Checks the last position of the road. If there is someone passing through the gate
        if (road[0] != null && road[0].isWorking()) {
            Vehicle vehicle = road[0];
            //This controls the time a vehicle takes to pass through the gate
            this.timeLeftToCharge--;

            if (timeLeftToCharge != 0) {
                task = new Task(this.uuid, 0, vehicle.getTypeOfVehicle(), vehicle.getUuid(), vehicle.getAge(), vehicle.getPriority(), "Pasó un instante en la caja");
                report.addTask(task);
            }
            //Increases the age of the vehicle. This is to measure the spent time
            vehicle.increaseAge();

            if (timeLeftToCharge == 0) { //If the vehicle has passed
                task = new Task(this.uuid, 0, vehicle.getTypeOfVehicle(), vehicle.getUuid(), vehicle.getAge(), vehicle.getPriority(), "Se le cobró lo correspondiente");
                report.addTask(task);

                usedCapacity--; //Reduces the vehicles on road
                road[0] = null; //Eliminates the vehicle

                timeLeftToCharge = timeToCharge; //Restores time to pay
            }
        } else if (road[0] != null && !road[0].isWorking()) {
            LinkedList<Vehicle> stuckVehicles = new LinkedList<>();
            for (int position = 1; position < road.length; position++) {
                if (road[position] != null) {
                    stuckVehicles.addLast(road[position]);
                }
                road[position] = null;
            }
            setWorking(false);
            manager.returnVehiclesToPlanner(stuckVehicles);
        } else {
            int position;
            for (position = 1; position < road.length; position++) { //Checks all position from the end to the beginning
                if (road[position] != null && road[position].isWorking()) { //If there is a vehicle
                    road[position].increaseAge(); //Increases spent time

                    if (road[position - 1] == null) { //If there is no one in front of him
                        //Report movement
                        task = new Task(this.uuid, position, road[position].getTypeOfVehicle(), road[position].getUuid(), road[position].getAge(), road[position].getPriority(), "Se movio de pos: " + position + " a pos: " + (position - 1));
                        report.addTask(task);

                        road[position - 1] = road[position]; //Moves vehicle
                        road[position] = null; //The previous position of the vehicle ends empty
                    } else {
                        task = new Task(this.uuid, position, road[position].getTypeOfVehicle(), road[position].getUuid(), road[position].getAge(), road[position].getPriority(), "Espera en posición: " + position);
                        report.addTask(task);
                    }
                } else if (road[position] != null && !road[position].isWorking()) {
                    break;
                }
            }

            if (position < road.length && road[position] != null && !road[position].isWorking()) {
                LinkedList<Vehicle> stuckVehicles = new LinkedList<>();
                for (int positionFromBrokenCar = position + 1; positionFromBrokenCar < road.length; positionFromBrokenCar++) {
                    if (road[positionFromBrokenCar] != null) {
                        stuckVehicles.addLast(road[positionFromBrokenCar]);
                    }
                    road[positionFromBrokenCar] = null;
                }
                setWorking(false);
                manager.returnVehiclesToPlanner(stuckVehicles);
            }


        }

        return report;
    }

    /**
     * Adds a vehicle in the beginning of the road only if the road isn't full. road[road.size]
     *
     * @param vehicle Vehicle that is going to be added to the road
     */
    public void addVehicleToRoad(Vehicle vehicle) {
        if (roadIsNotFull()) {
            usedCapacity++;
            this.road[road.length - 1] = vehicle;
        }
    }

    /**
     * Checks the first position of the road
     *
     * @return True if a vehicle can be added
     */
    public boolean roadIsNotFull() {
        return this.road[road.length - 1] == null;
    }

    /**
     * Return true if there isn't a car in the gate´s road
     *
     * @return true if road is empty
     */
    public boolean roadIsEmpty() {
        return usedCapacity == 0;
    }

}
