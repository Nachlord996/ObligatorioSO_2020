package org.ucu.fit.so;

public class Vehicle {
    private String typeOfVehicle;
    private String uuid;
    private static int numOfCars;
    private int age;
    private int priority;

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
