package org.ucu.fit.so;

public class Vehicle {
    private final String typeOfVehicle;
    private final String uuid;
    private static int numOfCars;
    private int timeWaited=-1;

    public int getAge() {
        return age;
    }

    public int getPriority() {
        return priority;
    }

    private int age;
    private int priority;
    private boolean working = true;
    private int counterBreak=-1;
    private int counterRepair=-1;
    private boolean recentlyRepair = false;

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isRecentlyRepair() {
        return recentlyRepair;
    }

    public void setRecentlyRepair(boolean recentlyRepair) {
        this.recentlyRepair = recentlyRepair;
    }

    public boolean isWorking() {
        return working;
    }

    /**
     * Class that represents a vehicle
     * @param typeOfVehicle Type of the vehicle
     * @param priority Priority of the vehicle (this will be used to multi queue planning)
     */
    public Vehicle(String typeOfVehicle, int priority) {
        this.typeOfVehicle = typeOfVehicle;
        this.uuid = "V" + getNumOfCars();
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

    public void setCounterBreak(int counterBreak) {
        this.counterBreak = counterBreak;
    }

    public void setCounterRepair(int counterRepair) {
        this.counterRepair = counterRepair;
    }

    private void updateIsWorking(){
        if(working){
            counterBreak--;
            if(counterBreak==1){
                working = false;
                counterBreak = -1;
            }
        }
        else{
            if(counterRepair==0){
                working = true;
                counterRepair = -1;
            }
            counterRepair--;
        }
    }
    /**
     * Increases vehicle´s age. This is used to measure spent time in queue and road
     * TO BE UPDATED. This will be used in order to rise the vehicle priority
     */
    public void increaseAge() {
        age++;
        updateIsWorking();
    }

    public int getTimeWaited() {
        return timeWaited;
    }

    public void increaseTimeWaited() {
        timeWaited++;
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
