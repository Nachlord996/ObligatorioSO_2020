package org.ucu.fit.so;

public class Task {
    private String gateID;
    private String vehicleID;
    private int timeWaiting;
    private int priority;
    private String action;
    private int instant;
    private int position;

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
        builder.append("," + gateID);
        builder.append("," + position);
        builder.append("," + vehicleID);
        builder.append("," + priority);
        builder.append("," + timeWaiting);
        builder.append("," + action);
        return new String(builder);
    }

}
