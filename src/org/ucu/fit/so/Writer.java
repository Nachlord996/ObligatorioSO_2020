package org.ucu.fit.so;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Writer {
    public static void write(String path, String text) {
        FileWriter fr;
        BufferedWriter br;
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
