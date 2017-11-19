/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Generator;
import hr.foi.uzdiz.antbaric.zadaca.components.Inspector;
import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.components.CSVParser;
import hr.foi.uzdiz.antbaric.zadaca.iterators.PlaceIterator;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UEntry;
import hr.foi.uzdiz.antbaric.zadaca.iterators.UIterator;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.AlgorithmEnum;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 *
 * @author javert
 */
public class Worker extends Thread implements Inspector {

    private static volatile Worker INSTANCE;
    private static Configuration CONFIG = null;
    private static AlgorithmEnum ALGORITHM;
    private static PlaceIterator PLACES;
    private static List<Device> SENSORS;
    private static List<Device> ACTUATORS;
    private static HashMap<String, Integer> FAILED_DEVICES;

    static {
        INSTANCE = new Worker();
    }

    private Worker() {
    }

    public static Worker getInstance(AlgorithmEnum algorithm) {
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
        CSVParser adapter = new CSVParser(Worker.CONFIG.getPlacesFilePath(), Worker.CONFIG.getActuatorsFielPath(), Worker.CONFIG.getSensorsFilePath());
        Logger.getInstance().log("Reading CSV files...", true);
        Worker.PLACES = new PlaceIterator();
        Worker.PLACES.add(adapter.getPlaces());
        Worker.SENSORS = adapter.getSensors();
        Worker.ACTUATORS = adapter.getActuators();
        Worker.FAILED_DEVICES = new HashMap<>();

        Logger.getInstance().log("Worker Thread started", true);

        this.configSystem();
        this.initSystem();

        for (Integer i = 0; i < Worker.CONFIG.getExecutionLimit(); i++) {
            Long startTimestamp = System.currentTimeMillis() / 1000;

            Logger.getInstance().log("Working, interval #" + (i + 1), true);

            for (final UIterator<UEntry<String, Place>> iterator = Worker.PLACES.getIterator(Worker.ALGORITHM); iterator.hasNext();) {
                final UEntry<String, Place> entry = iterator.next();
                Place place = entry.getValue();
                List<Device> sensors = this.getSensorsByCategory(place.getCategory());
                Logger.getInstance().log("Checking Place '" + place.getName() + "'", true);

                for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                    Sensor sensor = (Sensor) deviceIterator.next();

                    if (!this.activateDevice(place.getName(), sensor)) {
                        deviceIterator.remove();
                        Logger.getInstance().log("Replacing Sensor '" + sensor.getName() + "'", true);
                        sensor = (Sensor) sensor.prototype();

                        if (sensor.getStatus() == 1) {
                            deviceIterator.add(sensor);
                            Logger.getInstance().log("Init OK '" + sensor.getName() + "'", true);
                        } else {
                            Logger.getInstance().log("Init FAILED '" + sensor.getName() + "'", true);
                        }
                    }
                }

                for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                    Actuator actuator = (Actuator) deviceIterator.next();

                    if (actuator.isSensorChanged()) {
                        if (!this.activateDevice(place.getName(), actuator)) {
                            deviceIterator.remove();
                            Logger.getInstance().log("Replacing Actuator '" + actuator.getName() + "'", true);
                            actuator = (Actuator) actuator.prototype();

                            if (actuator.getStatus() == 1) {
                                deviceIterator.add(actuator);
                                Logger.getInstance().log("Init OK '" + actuator.getName() + "'", true);
                            } else {
                                Logger.getInstance().log("Init FAILED '" + actuator.getName() + "'", true);
                            }
                        }
                    } else {
                        Logger.getInstance().log("No change to sensors Actuator '" + actuator.getName() + "'", true);
                        actuator.activate();
                        Worker.FAILED_DEVICES.put(place.getName() + "." + actuator.getName(), 0);
                    }

                }

                entry.setValue(place);
                iterator.set(entry);

                if (this.isLimitExceeded(startTimestamp)) {
                    // break; TODO enable before prod 
                }
            }
        }

        Logger.getInstance().log("Writing log to output file...", true);
        Logger.getInstance().writeToFile();
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private void configSystem() {
        for (final UIterator<UEntry<String, Place>> iterator = Worker.PLACES.getIterator(AlgorithmEnum.INDEX); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            place.setId();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            List<Device> actuators = this.getActuatorsByCategory(place.getCategory());
            Logger.getInstance().log("Placing devices at '" + place.getName() + "'", true);

            if (!Worker.SENSORS.isEmpty()) {
                for (Integer i = 0; i < place.getSensorsNum(); i++) {
                    place.addSensor(sensors.get(Generator.getInstance().selectFrom(sensors)).prototype());
                }
            }

            if (!Worker.ACTUATORS.isEmpty()) {
                for (Integer i = 0; i < place.getActuatorsNum(); i++) {
                    place.addActuator(actuators.get(Generator.getInstance().selectFrom(actuators)).prototype());
                }
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    private void initSystem() {
        for (final UIterator<UEntry<String, Place>> iterator = Worker.PLACES.getIterator(AlgorithmEnum.SEQUENTIAL); iterator.hasNext();) {
            final UEntry<String, Place> entry = iterator.next();
            Place place = entry.getValue();
            List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            Logger.getInstance().log("Init devices at '" + place.getName() + " ID: " + place.getId() + "'", true);

            for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                final Device sensor = deviceIterator.next();

                if (sensor.getStatus() == 0) {
                    deviceIterator.remove();
                    Logger.getInstance().log("  Init FAILED '" + sensor.getNameAndId() + "'", true);
                } else {
                    Worker.FAILED_DEVICES.put(place.getName() + "." + sensor.getName(), 0);
                    Logger.getInstance().log("  Init OK '" + sensor.getNameAndId() + "'", true);
                }
            }

            for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                final Device actuator = deviceIterator.next();

                if (actuator.getStatus() == 0) {
                    deviceIterator.remove();
                    Logger.getInstance().log("  Init FAILED '" + actuator.getNameAndId() + "'", true);
                } else {
                    Worker.FAILED_DEVICES.put(place.getName() + "." + actuator.getName(), 0);
                    Logger.getInstance().log("  Init OK '" + actuator.getNameAndId() + "'", true);

                    this.configActuator((Actuator) actuator, place.getSensors());
                }
            }

            entry.setValue(place);
            iterator.set(entry);
        }
    }

    private void configActuator(Actuator actuator, List<Device> sensors) {
        Logger.getInstance().log("  Assigning Sensors to Actuator '" + actuator.getNameAndId() + "'", true);

        for (int i = 0; i < Generator.getInstance().fromInterval(1, sensors.size()); i++) {
            while (true) {
                Sensor sensor = (Sensor) sensors.get(Generator.getInstance().selectFrom(sensors));

                if (!actuator.sensors.contains(sensor)) {
                    actuator.sensors.add(sensor);
                    Logger.getInstance().log("      Added Sensor '" + sensor.getNameAndId() + "' to Actuator '" + actuator.getNameAndId() + "'", true);
                    break;
                }
            }
        }
    }

    private List<Device> getSensorsByCategory(Integer category) {
        List<Device> sensors = new ArrayList();

        for (Device sensor : Worker.SENSORS) {
            if (Objects.equals(sensor.getType(), category) || Objects.equals(sensor.getType(), 2)) {
                sensors.add(sensor);
            }
        }

        return sensors;
    }

    private List<Device> getActuatorsByCategory(Integer category) {
        List<Device> actuators = new ArrayList();

        for (Device actuator : Worker.ACTUATORS) {
            if (Objects.equals(actuator.getType(), category) || Objects.equals(actuator.getType(), 2)) {
                actuators.add(actuator);
            }
        }

        return actuators;
    }

    private Boolean activateDevice(String placeName, Device device) {
        String deviceId = placeName + "." + device.getName();
        Integer overload = Worker.FAILED_DEVICES.get(deviceId);

        if (device.getStatus() == 0) {
            overload = overload == null ? 1 : overload + 1;

            if (overload.equals(3)) {
                Worker.FAILED_DEVICES.remove(deviceId);
                Logger.getInstance().log("Device '" + device.getName() + "ID: " + device.getId() + "'@'" + placeName + "' need replacement...", true);

                return false;
            }
        } else {
            overload = 0;
            device.activate();
        }

        Worker.FAILED_DEVICES.put(deviceId, overload);

        return true;
    }

    private Boolean isLimitExceeded(Long startTimestamp) {
        return System.currentTimeMillis() - startTimestamp > Worker.CONFIG.getInterval() * 1000;
    }

}
