package org.ucu.fit.so;

public class TollGate extends Gate {

    private final Vehicle[] road;
    private final int timeToCharge;
    private int timeLeftToCharge;
    private int usedCapacity;

    TollGate(int gateNumber, int roadSize, int timeToPay) {
        super(gateNumber);
        this.road = new Vehicle[roadSize];
        this.timeToCharge = timeToPay;
        this.timeLeftToCharge = timeToPay;
        this.usedCapacity = 0;
    }

    @Override
    TaskReport consume() {
        TaskReport report = new TaskReport();
        Task task;
        if (road[0] != null) {
            task = new Task(this.uuid, 0, road[0].getUuid(), road[0].getAge(), road[0].getPriority(), "Pasó un instante en la caja");
            report.addTask(task);
            this.timeLeftToCharge--;
            road[0].increaseAge();
            if (timeLeftToCharge == 0) {
                task = new Task(this.uuid, 0, road[0].getUuid(), road[0].getAge(), road[0].getPriority(), "Se le cobró lo correspondiente");
                report.addTask(task);
                usedCapacity--;
                road[0] = null;
                timeLeftToCharge = timeToCharge;
            }
        }

        for (int position = 1; position < road.length; position++) {
            if (road[position] != null) {
                road[position].increaseAge();

                if (road[position - 1] == null) {
                    task = new Task(this.uuid, position, road[position].getUuid(), road[position].getAge(), road[position].getPriority(), "Se movio de pos: " + position + " a pos: " + (position - 1));
                    report.addTask(task);
                    road[position - 1] = road[position];
                    road[position] = null;
                }
            }
        }
       // System.out.println(this.uuid);
        return report;
    }

    /**
     * Adds a vehicle in the beginning of the road only if the road isn't full. road[road.size]
     * @param vehicle Vehicle that is going to be added to the road
     */
    public void addVehicleToRoad(Vehicle vehicle){
        if (!roadIsFull()){
            usedCapacity++;
            this.road[road.length - 1] = vehicle;
        }
    }

    /**
     * Checks the first position of the road
     * @return True if a vehicle can be added
     */
    public boolean roadIsFull(){
        return this.road[road.length - 1] != null;
    }

    public boolean roadIsEmpty(){
        return usedCapacity == 0;
    }

}
