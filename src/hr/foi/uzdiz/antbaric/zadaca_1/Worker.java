/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1;

import hr.foi.uzdiz.antbaric.zadaca_1.components.CSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.components.UzDizCSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Place;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author javert
 */
public class Worker extends Thread {

    private static volatile Worker INSTANCE;
    private static Configuration CONFIG = null;

    static {
        INSTANCE = new Worker();
    }

    private Worker() {
    }

    public static Worker getInstance() {
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
        HashMap<String, Place> places = adapter.getPlaces();
        List<Device> sensors = adapter.getSensors();
        List<Device> actuators = adapter.getActuators();
        
        for (Integer i = 0; i < Worker.CONFIG.getExecutionLimit(); i++) {
            try {
                System.out.println("Working...");

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

}
