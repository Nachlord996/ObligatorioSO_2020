package org.ucu.fit.so;

public class Vehicle {
    private String typeOfVehicle;
    private String uuid;
    private static int numOfCars;
    public Vehicle(String typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
        this.uuid = "V" + getNumOfCars();
    }
    private int getNumOfCars(){
        numOfCars++;
        return numOfCars;
    }

    public String getUuid(){
        return uuid;
    }

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }
}
