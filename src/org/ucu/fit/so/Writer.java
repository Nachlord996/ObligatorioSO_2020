package org.ucu.fit.so;

import java.io.*;
import java.util.LinkedList;

public class Writer {
    public static void write(String path, String text) {
        FileWriter fr = null;
        BufferedWriter br = null;
        try {
            fr = new FileWriter(path);
            br = new BufferedWriter(fr);

            br.write(text);
            br.close();
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
