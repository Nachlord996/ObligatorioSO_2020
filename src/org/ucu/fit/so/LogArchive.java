package org.ucu.fit.so;

import java.util.LinkedList;

public class LogArchive extends LinkedList<String> {

    public String getLogMessage(){
        StringBuilder builder = new StringBuilder();
        for (String i : this){
            builder.append(i + '\n');
    }
        return new String(builder);
    }

}
