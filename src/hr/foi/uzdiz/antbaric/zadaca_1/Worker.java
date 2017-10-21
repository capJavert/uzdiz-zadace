/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca_1;

import hr.foi.uzdiz.antbaric.zadaca_1.algorithms.Algorithm;
import hr.foi.uzdiz.antbaric.zadaca_1.components.CSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.components.Generator;
import hr.foi.uzdiz.antbaric.zadaca_1.components.Inspector;
import hr.foi.uzdiz.antbaric.zadaca_1.components.UzDizCSVAdapter;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Device;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Place;
import hr.foi.uzdiz.antbaric.zadaca_1.models.Sensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
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
    private static List<Map.Entry<String, Place>> PLACES;
    private static List<Device> SENSORS;
    private static List<Device> ACTUATORS;
    private static HashMap<String, Integer> FAILED_DEVICES;

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
        Worker.PLACES = adapter.getPlaces();
        Worker.SENSORS = adapter.getSensors();
        Worker.ACTUATORS = adapter.getActuators();
        Worker.FAILED_DEVICES = new HashMap<>();

        this.configSystem();
        this.initSystem();
        this.order();

        for (Integer i = 0; i < Worker.CONFIG.getExecutionLimit(); i++) {
            try {
                System.out.println("Working...");

                for (final ListIterator<Map.Entry<String, Place>> iterator = Worker.PLACES.listIterator(); iterator.hasNext();) {
                    final Map.Entry<String, Place> entry = iterator.next();
                    Place place = entry.getValue();
                    List<Device> sensors = this.getSensorsByCategory(place.getCategory());

                    for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                        Sensor sensor = (Sensor) deviceIterator.next();

                        if (!this.activateDevice(place.getName(), sensor)) {
                            deviceIterator.remove();
                            sensor = (Sensor) sensor.prototype();

                            if (sensor.getStatus() == 1) {
                                deviceIterator.add(sensor);
                            }
                        };
                    }

                    for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                        Actuator actuator = (Actuator) deviceIterator.next();

                        if (!this.activateDevice(place.getName(), actuator)) {
                            deviceIterator.remove();
                            actuator = (Actuator) actuator.prototype();

                            if (actuator.getStatus() == 1) {
                                deviceIterator.add(actuator);
                            }
                        };
                    }

                    entry.setValue(place);
                    iterator.set(entry);
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

    private void configSystem() {
        Generator generator = Generator.getInstance();

        for (final ListIterator<Map.Entry<String, Place>> iterator = Worker.PLACES.listIterator(); iterator.hasNext();) {
            final Map.Entry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            List<Device> actuators = this.getActuatorsByCategory(place.getCategory());

            for (Integer i = 0; i < place.getSensorsNum(); i++) {
                place.addSensor(sensors.get(generator.selectFrom(sensors)));
            }

            for (Integer i = 0; i < place.getActuatorsNum(); i++) {
                place.addActuator(actuators.get(generator.selectFrom(actuators)));
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    private void initSystem() {
        Generator generator = Generator.getInstance();

        for (final ListIterator<Map.Entry<String, Place>> iterator = Worker.PLACES.listIterator(); iterator.hasNext();) {
            final Map.Entry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());

            for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                final Device sensor = deviceIterator.next();

                if (sensor.getStatus() == 0) {
                    deviceIterator.remove();
                } else {
                    Worker.FAILED_DEVICES.put(place.getName() + "." + sensor.getName(), 0);
                }
            }

            for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                final Device actuator = deviceIterator.next();

                if (actuator.getStatus() == 0) {
                    deviceIterator.remove();
                } else {
                    Worker.FAILED_DEVICES.put(place.getName() + "." + actuator.getName(), 0);
                }
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    private List<Device> getSensorsByCategory(Integer category) {
        List<Device> sensors = new ArrayList();

        for (Device sensor : Worker.SENSORS) {
            if (Objects.equals(sensor.getCategory(), category)) {
                sensors.add(sensor);
            }
        }

        return sensors;
    }

    private List<Device> getActuatorsByCategory(Integer category) {
        List<Device> actuators = new ArrayList();

        for (Device actuator : Worker.ACTUATORS) {
            if (Objects.equals(actuator.getCategory(), category)) {
                actuators.add(actuator);
            }
        }

        return actuators;
    }

    private Boolean activateDevice(String placeName, Device device) {
        String deviceId = placeName + "." + device.getName();
        Integer overload = Worker.FAILED_DEVICES.get(deviceId);

        if (device.getStatus() == 0) {
            overload++;

            if (overload.equals(3)) {
                Worker.FAILED_DEVICES.remove(deviceId);

                return false;
            }
        } else {
            overload = 0;
            device.activate();
        }

        Worker.FAILED_DEVICES.put(deviceId, overload);

        return true;
    }

    @Override
    public void order() {
        Worker.PLACES = Worker.ALGORITHM.order(Worker.PLACES);
    }

}
