package org.ucu.fit.so;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Program {

    private static HashMap<String, Object> CONFIG;
    private static String THREADS_NUMBER_KEY = "TOLL_LANES_NUMBER";
    private static int THREADS_NUMBER;
    private static HashMap<String,TollGate> TOLL_GATES;
    public static TimeCounter TIMER;
    public static Manager PROCESS_MANAGER;
    private static Planner planner;
    private static LogArchive LOG_ARCHIVE;
    private static String OUTPUT_TEXT_PATH;

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
        OUTPUT_TEXT_PATH = String.valueOf(CONFIG.get("OUTPUT_PATH"));

        TOLL_GATES = new HashMap<>();
        int chargeTime = 2;
        int roadSize = 3;


        for (int i = 0; i < THREADS_NUMBER; i++){
            TollGate gate = new TollGate(i,roadSize,chargeTime);
            TOLL_GATES.put(gate.uuid, gate);
        }

        LOG_ARCHIVE = new LogArchive();

        HashMap<String,Integer> vehiclesPrioritiesParsed = new HashMap<>();
        HashMap<String,Object> vehiclesPriorities = builder.buildDictionary("src/config/vehiclePriorities.csv");
        for(String key:vehiclesPriorities.keySet()){
            vehiclesPrioritiesParsed.put(key,Integer.parseInt(vehiclesPriorities.get(key).toString()));
        }
        HashMap<Integer,LinkedList<Vehicle>> vehiclesForTime = new HashMap<>();
        for(String line : Reader.read("src/data/vehicles.csv")){
            String[] array = line.split(",");

            int amountVehicles;

            try{
                amountVehicles = Integer.parseInt(array[2]);
            }catch (Exception e){
                continue;
            }
            int amountTime = Integer.parseInt(array[0]);
            String carType = array[1];

            if(!vehiclesForTime.containsKey(amountTime)){
                vehiclesForTime.put(amountTime,new LinkedList<>());
            }
            for(int i =0 ; i < amountVehicles; i++) {
                Vehicle vehicle = new Vehicle(carType,vehiclesPrioritiesParsed.get(carType));
                vehiclesForTime.get(amountTime).add(vehicle);
            }
        }
        planner = new Planner(vehiclesPrioritiesParsed,vehiclesForTime);
    }

    private static void start(){
        try {
            TIMER = new TimeCounter();
            PROCESS_MANAGER = new Manager(TIMER, TOLL_GATES, planner, LOG_ARCHIVE);
            PROCESS_MANAGER.begin();

            while(PROCESS_MANAGER.stillRunning()) { }

            Writer.write(OUTPUT_TEXT_PATH,LOG_ARCHIVE.getLogMessage());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
