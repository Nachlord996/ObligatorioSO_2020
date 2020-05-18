package org.ucu.fit.so;

public class Task {
    private final String gateID;
    private final String vehicleID;
    private final int timeWaiting;
    private final int priority;
    private final String action;
    private int instant;
    private final int position;

    public Task(String gateID, int position, String vehicleID, int timeWaiting, int priority, String action) {
        this.gateID = gateID;
        this.vehicleID = vehicleID;
        this.timeWaiting = timeWaiting;
        this.priority = priority;
        this.action = action;
        this.position = position;
        instant = -1;
    }

    public void setInstant(int instant){
        if (instant < 0){
            throw new IllegalArgumentException("Time instant can't be negative");
        }
        this.instant = instant;
    }

    public String getReportMessage(){
        return instant == -1 ? null : generateReport();
    }

    private String generateReport() {
        StringBuilder builder = new StringBuilder();
        builder.append(instant);
        builder.append(",").append(gateID);
        builder.append(",").append(position);
        builder.append(",").append(vehicleID);
        builder.append(",").append(priority);
        builder.append(",").append(timeWaiting);
        builder.append(",").append(action);
        return new String(builder);
    }

}
