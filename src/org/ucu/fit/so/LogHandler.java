package org.ucu.fit.so;

import java.util.LinkedList;

public class LogHandler {

    private final LinkedList<String> logfile = new LinkedList<>();

    public LogHandler() {
        String header = "Instant, Gate ID, Position, Vehicle Type, Vehicle ID, Priority, Time On Road,Time On Preselector, Action";
        log(header);
    }

    public void log(String message){
        logfile.add(message);
    }

    public LinkedList<String> getLogfile() {
        return logfile;
    }
}
