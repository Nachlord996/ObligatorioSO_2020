package org.ucu.fit.so;

import java.util.LinkedList;

public class LogHandler {

    private final LogArchive logfile = new LogArchive();

    public void log(String message){
        logfile.add(message);
    }

    public LogArchive getLogfile() {
        return logfile;
    }
}
