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

    public Vehicle(String typeOfVehicle, int priority) {
        this.typeOfVehicle = typeOfVehicle;
        this.uuid = "V" + getNumOfCars();
        this.age = 0;
        this.priority = priority;
    }

    private int getNumOfCars(){
        numOfCars++;
        return numOfCars;
    }

    public void increaseAge() {
        age++;
    }

    public String getUuid(){
        return uuid;
    }

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }
}
