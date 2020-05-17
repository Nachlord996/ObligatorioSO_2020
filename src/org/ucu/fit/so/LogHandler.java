package org.ucu.fit.so;

import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler {

    private LinkedList<String> logfile;

    public LogHandler(LinkedList<String> log){
      logfile = log;
    }

    public void log(String message){
        logfile.add(message);
    }
}
