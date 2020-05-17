package org.ucu.fit.so;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TxtDictionaryBuilder will generate a map from a configuration plain text file.
 *
 * @author  Ignacio Martínez
 * @version 1.4
 * @since   2020-01-23
 */

public class TxtDictionaryBuilder implements IDictionaryBuilder {

    private String dictionaryPath;

    @Override
    public HashMap<String, Object> buildDictionary(String filepath) {
        dictionaryPath = filepath;
        HashMap<String, Object> dictionary = null;
        String[] parts;
        String key;
        String value;

        if (fileExists()){
            ArrayList<String> lines = openFile();
            if (lines != null){
                dictionary = new HashMap<>();
                for (String ln : lines){
                    parts = ln.split(",");
                    key = parts[0];
                    value = parts[1];
                    dictionary.put(key, value);
                }
            }
        }
        return dictionary;
    }
    private boolean fileExists() {
        boolean exists = false;
        if (dictionaryPath != null) {
            File f = new File(dictionaryPath);
            if (f.exists() && !f.isDirectory()) {
                exists = true;
            }
        }
        return exists;
    }

    private boolean checkIntegrity(String line) {
        if (line.contains(",")){
            String[] parts = line.split(",");
            return parts.length == 2;
        }
        return false;
    }

    private ArrayList openFile() {
        ArrayList linesOfFile = new ArrayList();
        try{
            FileReader fr = new FileReader(dictionaryPath);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null){
                if (line.equals("")){
                    line = br.readLine();
                    continue;
                }
                if (line.startsWith("//")){
                    line = br.readLine();
                    continue;
                }
                if (!checkIntegrity(line)){
                    linesOfFile = null;
                    break;
                }
                linesOfFile.add(line);
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesOfFile;
    }
}
