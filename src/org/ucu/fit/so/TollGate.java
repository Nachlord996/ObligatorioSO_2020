package org.ucu.fit.so;

public class TollGate extends Gate {

    private Vehicle[] road;
    private final int timeToCharge;
    private int timeLeftToCharge;

    TollGate(int gateNumber, int roadSize, int timeToPay) {
        super(gateNumber);
        this.road = new Vehicle[roadSize];
        this.timeToCharge = timeToPay;
        this.timeLeftToCharge = timeToPay;
    }

    @Override
    TaskReport consume() {
        if (road[0] != null){
            this.timeLeftToCharge--;
            road[0].increaseAge();
            if (timeLeftToCharge == 0){
                road[0] = null;
                //Hacer log
                timeLeftToCharge = timeToCharge;
            }
            for (int position = 1; position < road.length ; position++) {
                if (road[position - 1] == null){
                    road[position - 1] = road[position];
                    road[position] = null;
                    //Generar Log
                }
            }
        }
        System.out.println(this.uuid);
        return new TaskReport();
    }

    /**
     * Adds a vehicle in the beginning of the road only if the road isn't full. road[road.size]
     * @param vehicle Vehicle that is going to be added to the road
     */
    public void addVehicleToRoad(Vehicle vehicle){
        if (!roadIsFull()){
            this.road[road.length - 1] = vehicle;
        }
    }

    /**
     * Checks the first position of the road
     * @return True if a vehicle can be added
     */
    public boolean roadIsFull(){
        return this.road[road.length - 1] == null;
    }

}
