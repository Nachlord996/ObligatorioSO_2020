package org.ucu.fit.so;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class Program {

    private static final HashMap<String,TollGate> TOLL_GATES = new HashMap<>();
    private static Planner PLANNER;
    private static String OUTPUT_TEXT_PATH;
    private static int AMOUNT_PRIORITIES;

    public static void main(String[] args){
        try {
            initialize();
            System.out.println("Successfully Initialized");
            start();
        } catch (InitialConfigurationException init){
            System.out.println(init.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initialize() throws InitialConfigurationException {
        String THREADS_NUMBER_KEY = "TOLL_LANES_NUMBER";
        String CHARGE_TIME_KEY = "CHARGE_TIME";
        String ROAD_SIZE_KEY = "ROAD_SIZE";
        String INPUT_FILE_KEY = "INPUT_FILE";

        IDictionaryBuilder builder = new TxtDictionaryBuilder();
        HashMap<String, Object> CONFIG = builder.buildDictionary("src/config/INIT_CONFIG.txt");
        HashMap<String, Object> V_PRIORITIES = builder.buildDictionary("src/config/vehiclePriorities.csv");
        HashMap<String, Integer> vehiclesPrioritiesParsed = new HashMap<>();
        HashMap<Integer, LinkedList<Vehicle>> vehiclesForTime = new HashMap<>();


        int threadNumber;
        int chargeTime;
        int roadSize;

        if (CONFIG == null) {
            throw new InitialConfigurationException("Config file not found or corrupted");
        }

        if (V_PRIORITIES == null) {
            throw new InitialConfigurationException("Config file not found or corrupted");
        }

        if (!CONFIG.containsKey(THREADS_NUMBER_KEY)) {
            throw new InitialConfigurationException("Missing parameter for lanes of tollgate");
        }
        try {
            threadNumber = Integer.parseInt(CONFIG.get(THREADS_NUMBER_KEY).toString());
        } catch (Exception e) {
            throw new InitialConfigurationException("Incorrect value type for lanes of tollgate");
        }

        if (!CONFIG.containsKey(CHARGE_TIME_KEY)) {
            throw new InitialConfigurationException("Missing parameter for charger time of tollgate");
        }
        try {
            chargeTime = Integer.parseInt(CONFIG.get(CHARGE_TIME_KEY).toString());
        } catch (Exception e) {
            throw new InitialConfigurationException("Incorrect value type for charger time of tollgate");
        }

        if (!CONFIG.containsKey(ROAD_SIZE_KEY)) {
            throw new InitialConfigurationException("Missing parameter for road size of tollgate");
        }
        try {
            roadSize = Integer.parseInt(CONFIG.get(ROAD_SIZE_KEY).toString());
        } catch (Exception e) {
            throw new InitialConfigurationException("Incorrect value type for road size of tollgate");
        }

        if (!CONFIG.containsKey(INPUT_FILE_KEY)) {
            throw new InitialConfigurationException("Missing parameter for inputFile");
        }

        //Here the file input should be selected
        File fileInput = new File(CONFIG.get(INPUT_FILE_KEY).toString());

        if (!fileInput.exists() || fileInput.isDirectory()) {
            throw new InitialConfigurationException("Input file not found or is a Directory");
        }

        OUTPUT_TEXT_PATH = "src/data/output/Output_" + fileInput.getName();

        //Instance Gates and put in TOLL_GATES
        TollGate gate;
        for (int i = 0; i < threadNumber; i++) {
            gate = new TollGate(i, roadSize, chargeTime);
            TOLL_GATES.put(gate.uuid, gate);
        }

        //Parse Value of vehiclesPriorities to Integer and put in vehiclesPrioritiesParsed
        String rawPriority;
        int priority;
        for (String key : V_PRIORITIES.keySet()) {
            rawPriority = V_PRIORITIES.get(key).toString();
            try {
                priority = Integer.parseInt(rawPriority);
                vehiclesPrioritiesParsed.put(key, priority);
                if (AMOUNT_PRIORITIES < priority) {
                    AMOUNT_PRIORITIES = priority;
                }
            } catch (Exception e) {
                throw new InitialConfigurationException("Invalid value for priority at: " + key + ", " + rawPriority);
            }
        }

        //Read the vehicles file and put them in vehiclesForTime
        int amountVehicles;
        int amountTime;
        String carType;
        String[] array;
        Vehicle vehicle;
        LinkedList<String> linesVehicles = Reader.read(fileInput.getPath());

        HashMap<String,Vehicle> vehicleHashMap = new HashMap<>();

        for (String line : linesVehicles) {
            array = line.split(",");
            try {
                amountVehicles = Integer.parseInt(array[2]);
                amountTime = Integer.parseInt(array[0]);
                carType = array[1];
            } catch (Exception e) {
                continue;
            }
            if (!V_PRIORITIES.containsKey(carType)){
                continue;
            }
            if (!vehiclesForTime.containsKey(amountTime)) {
                vehiclesForTime.put(amountTime, new LinkedList<>());
            }
            for (int i = 0; i < amountVehicles; i++) {
                vehicle = new Vehicle(carType, vehiclesPrioritiesParsed.get(carType));
                vehicleHashMap.put(vehicle.getUuid(),vehicle);
                vehiclesForTime.get(amountTime).add(vehicle);
            }
        }
        LinkedList<String> linesEvents = Reader.read("src/data/input/events.csv");
        for(String line : linesEvents){
            if(line.contains("#")){
                continue;
            }
            array = line.split(",");
            int action = Integer.parseInt(array[2]);
            int time = Integer.parseInt(array[0]);
            if(array[1].contains("G")){
                if(action==1){
                    TOLL_GATES.get(array[1]).setCounterRepair(time);
                }
                else if(action==0){
                    TOLL_GATES.get(array[1]).setCounterBreak(time);
                }
            }
            if(array[1].contains("V")){
                if(action==1){
                    vehicleHashMap.get(array[1]).setCounterRepair(time);
                }
                else if(action==0){
                    vehicleHashMap.get(array[1]).setCounterBreak(time);
                }
            }
        }

        // Initialize planner using priorities table and vehicles ordered by entry time
        PLANNER = new Planner(vehiclesPrioritiesParsed, vehiclesForTime,AMOUNT_PRIORITIES);
    }

    private static void start(){
        try {
            Manager PROCESS_MANAGER = new Manager(TOLL_GATES, PLANNER, OUTPUT_TEXT_PATH);
            PROCESS_MANAGER.begin();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
