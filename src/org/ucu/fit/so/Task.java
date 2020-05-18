package org.ucu.fit.so;

public class Task {
    private final String gateID;
    private final String vehicleID;
    private final int timeWaiting;
    private final int priority;
    private final String action;
    private int instant;
    private final int position;
    private final String vehicleType;

    /**
     * A task is created to save information about the simulation.
     * @param gateID The id where the event has occurred
     * @param position The position where the vehicle is
     * @param vehicleID The vehicle ID
     * @param timeWaiting The time it has waited
     * @param priority The vehicle priority
     * @param action The action it has performed
     */
    public Task(String gateID, int position,String vehicleType, String vehicleID, int timeWaiting, int priority, String action) {
        this.gateID = gateID;
        this.vehicleID = vehicleID;
        this.timeWaiting = timeWaiting;
        this.priority = priority;
        this.action = action;
        this.position = position;
        this.vehicleType = vehicleType;
        instant = -1;
    }

    /**
     * Set the instant of the task
     * @param instant
     */
    public void setInstant(int instant){
        if (instant < 0){
            throw new IllegalArgumentException("Time instant can't be negative");
        }
        this.instant = instant;
    }

    public String getReportMessage(){
        return instant == -1 ? null : generateReport();
    }

    /**
     * Returns a string with al the data together in csv format
     * @return String with data in csv format
     */
    private String generateReport() {
        StringBuilder builder = new StringBuilder();
        builder.append(instant);
        builder.append(",").append(gateID);
        builder.append(",").append(position);
        builder.append(",").append(vehicleType);
        builder.append(",").append(vehicleID);
        builder.append(",").append(priority);
        builder.append(",").append(timeWaiting);
        builder.append(",").append(action);
        return new String(builder);
    }

}
