package org.ucu.fit.so;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
public class Writer {

    /**
     * Writes a line in a file
     * @param path path of the file
     * @param listOfText list of text to append
     */
    public static void write(String path, LinkedList<String> listOfText) {
        FileWriter fr;
        BufferedWriter br;
        File file = new File(path);
        if (file.exists() && !file.isDirectory()){
            boolean deleted = false;
            while(!deleted){
                deleted = file.delete();
            }
        }
        try {
            fr = new FileWriter(path);
            br = new BufferedWriter(fr);

            for(String line:listOfText){
                br.write(line);
                br.newLine();
            }
            br.close();
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
