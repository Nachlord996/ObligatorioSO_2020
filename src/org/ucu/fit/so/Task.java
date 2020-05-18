package org.ucu.fit.so;

public class Task {
    private String gateID;
    private String vehicleID;
    private int timeWaiting;
    private int priority;
    private String action;
    private int instant;
    private int position;

    /**
     * A task is created to save information about the simulation.
     * @param gateID The id where the event has occurred
     * @param position The position where the vehicle is
     * @param vehicleID The vehicle ID
     * @param timeWaiting The time it has waited
     * @param priority The vehicle priority
     * @param action The action it has performed
     */
    public Task(String gateID, int position, String vehicleID, int timeWaiting, int priority, String action) {
        this.gateID = gateID;
        this.vehicleID = vehicleID;
        this.timeWaiting = timeWaiting;
        this.priority = priority;
        this.action = action;
        this.position = position;
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
        builder.append("," + gateID);
        builder.append("," + position);
        builder.append("," + vehicleID);
        builder.append("," + priority);
        builder.append("," + timeWaiting);
        builder.append("," + action);
        return new String(builder);
    }

}
