package org.ucu.fit.so;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class Reader {
        public static LinkedList<String> read(String path) {
            File file = null;
            FileReader fr = null;
            BufferedReader br = null;
            LinkedList<String> listRead = new LinkedList<>();
            try {
                file = new File (path);
                fr = new FileReader (file);
                br = new BufferedReader(fr);

                String linea;
                br.lines();
                while((linea=br.readLine())!=null) listRead.add(linea);
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    if( null != fr ){
                        fr.close();
                    }
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }
            return listRead;
        }

}
