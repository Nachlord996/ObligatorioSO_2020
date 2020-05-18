package org.ucu.fit.so;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Writer {

    /**
     * Writes a line in a file
     * @param path path of the file
     * @param text line to write
     */
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
