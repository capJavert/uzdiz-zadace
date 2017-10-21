/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1;

import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Algorithm;
import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Alphabetical;
import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Random;
import hr.foi.uzdiz.antbaric.zadaca_1.components.CSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.components.Inspector;
import hr.foi.uzdiz.antbaric.zadaca_1.components.UzDizCSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Place;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author javert
 */
public class Worker extends Thread implements Inspector {

    private static volatile Worker INSTANCE;
    private static Configuration CONFIG = null;
    private static Algorithm ALGORITHM;

    static {
        INSTANCE = new Worker();
    }

    private Worker() {
    }

    public static Worker getInstance(Algorithm algorithm) {
        Worker.ALGORITHM = algorithm;
        
        return INSTANCE;
    }

    public static Configuration getConfig() {
        return CONFIG;
    }

    public static void setConfig(Configuration config) {
        Worker.CONFIG = config;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        CSVAdapter adapter = new UzDizCSVAdapter(Worker.CONFIG.getPlacesFilePath(), Worker.CONFIG.getActuatorsFielPath(), Worker.CONFIG.getSensorsFilePath());
        List<Map.Entry<String, Place>> places = adapter.getPlaces();
        List<Device> sensors = adapter.getSensors();
        List<Device> actuators = adapter.getActuators();
        
        places = Worker.ALGORITHM.order(places);
       
        for (Integer i = 0; i < Worker.CONFIG.getExecutionLimit(); i++) {
            try {
                System.out.println("Working...");
                
                for(Map.Entry<String, Place> place : places) {
                    // do work
                }

                sleep(Worker.CONFIG.getInterval());
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public List<Map.Entry<String, Place>> order() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
