package org.ucu.fit.so;

public class LogHandler {

    private final LogArchive logfile = new LogArchive();

    public LogHandler() {
        String header = "Instant, Gate ID, Position, Vehicle ID, Priority, Time Waiting, Action";
        log(header);
    }

    public void log(String message){
        logfile.add(message);
    }

    public LogArchive getLogfile() {
        return logfile;
    }
}
