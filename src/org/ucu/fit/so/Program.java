package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Program {

    private static HashMap<String, Object> CONFIG;
    private static String THREADS_NUMBER_KEY = "TOLL_LANES_NUMBER";
    private static int THREADS_NUMBER;
    private static HashMap<String,Gate> TOLL_GATES;
    public static TimeCounter TIMER;
    public static Manager PROCESS_MANAGER;


    public static void main(String[] args){
        try {
            initialize();
            System.out.println("Inicializacion correcta");
            start();
        } catch (InitialConfigurationException init){
            System.out.println(init.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initialize() throws InitialConfigurationException {
        IDictionaryBuilder builder = new TxtDictionaryBuilder();
        CONFIG = builder.buildDictionary("src/config/INIT_CONFIG.txt");
        if (CONFIG == null) {
            throw new InitialConfigurationException("Config file not found or corrupted");
        }
        if (!CONFIG.containsKey(THREADS_NUMBER_KEY)){
            throw new InitialConfigurationException("Missing parameter for lanes of tollgate");
        } try {
            THREADS_NUMBER = Integer.parseInt(CONFIG.get(THREADS_NUMBER_KEY).toString());
        } catch (Exception e) {
            throw new InitialConfigurationException("Incorrect value type for lanes of tollgate");
        }
        TOLL_GATES = new HashMap<>();
        Gate gate = null;
        for (int i = 0; i < THREADS_NUMBER; i++){
            gate = new TollGate(i);
            TOLL_GATES.put(gate.uuid, gate);
        }
    }

    private static void start(){
        try {
            TIMER = new TimeCounter();
            PROCESS_MANAGER = new Manager(TIMER, TOLL_GATES);
            PROCESS_MANAGER.begin();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
