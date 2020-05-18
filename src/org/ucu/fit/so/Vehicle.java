package org.ucu.fit.so;

public class Vehicle {
    private final String typeOfVehicle;
    private final String uuid;
    private static int numOfCars;

    public int getAge() {
        return age;
    }

    public int getPriority() {
        return priority;
    }

    private int age;
    private final int priority;

    /**
     * Class that represents a vehicle
     * @param typeOfVehicle Tipe of the vehicle
     * @param priority Priority of the vehicle (this will be used to multi queue planning)
     */
    public Vehicle(String typeOfVehicle, int priority) {
        this.typeOfVehicle = typeOfVehicle;
        this.uuid = "V" + getNumOfCars();
        this.age = 0;
        this.priority = priority;
    }

    /**
     * The num of cars is used to create a unique id to each vehicle.
     * This method should only be called when the vehicle is created
     * @return int
     */
    private int getNumOfCars(){
        numOfCars++;
        return numOfCars;
    }

    /**
     * Increases vehicle´s age. This is used to measure spent time in queue and road
     * TO BE UPDATED. This will be used in order to rise the vehicle priority
     */
    public void increaseAge() {
        age++;
    }

    /**
     * Returns string with the unique id of the vehicle
     * @return String
     */
    public String getUuid(){
        return uuid;
    }

    /**
     * Returns type of the vehicle
     * @return String 
     */
    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }
}
