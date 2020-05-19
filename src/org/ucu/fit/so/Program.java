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
            System.out.println("Inicializacion correcta");
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
        
        IDictionaryBuilder builder = new TxtDictionaryBuilder();
        HashMap<String, Object> CONFIG = builder.buildDictionary("src/config/INIT_CONFIG.txt");
        HashMap<String,Integer> vehiclesPrioritiesParsed = new HashMap<>();
        HashMap<String,Object> vehiclesPriorities = builder.buildDictionary("src/config/vehiclePriorities.csv");
        HashMap<Integer,LinkedList<Vehicle>> vehiclesForTime = new HashMap<>();

        int threadNumber;
        int chargeTime;
        int roadSize;

        File fileInput = new File("src/data/input/vehicles.csv");

        if(!fileInput.exists() || fileInput.isDirectory()){
            throw new InitialConfigurationException("Input file not found or is a Directory");
        }

        OUTPUT_TEXT_PATH = "src/data/output/Output_"+fileInput.getName();


        if (CONFIG == null) {
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


        //Instance Gates and put in TOLL_GATES
        for (int i = 0; i < threadNumber; i++){
            TollGate gate = new TollGate(i,roadSize,chargeTime);
            TOLL_GATES.put(gate.uuid, gate);
        }

        //Parse Value of vehiclesPriorities to Integer and put in vehiclesPrioritiesParsed
        for(String key:vehiclesPriorities.keySet()){
            int priority = Integer.parseInt(vehiclesPriorities.get(key).toString());
            vehiclesPrioritiesParsed.put(key,priority);
            if(AMOUNT_PRIORITIES<priority){
                AMOUNT_PRIORITIES = priority;
            }
        }
        //Read the vehicles file and put them in vehiclesForTime
        for(String line : Reader.read(fileInput.getPath())){
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
        PLANNER = new Planner(vehiclesPrioritiesParsed,vehiclesForTime);

    }

    private static void start(){
        try {
            Manager PROCESS_MANAGER = new Manager(TOLL_GATES, PLANNER, OUTPUT_TEXT_PATH, AMOUNT_PRIORITIES);
            PROCESS_MANAGER.begin();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
