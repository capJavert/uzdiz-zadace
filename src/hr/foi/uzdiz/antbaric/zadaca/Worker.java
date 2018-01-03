/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca;

import hr.foi.uzdiz.antbaric.zadaca.helpers.Logger;
import hr.foi.uzdiz.antbaric.zadaca.components.CSVParser;
import hr.foi.uzdiz.antbaric.zadaca.extensions.Stateful;
import hr.foi.uzdiz.antbaric.zadaca.models.Actuator;
import hr.foi.uzdiz.antbaric.zadaca.models.Configuration;
import hr.foi.uzdiz.antbaric.zadaca.models.Device;
import hr.foi.uzdiz.antbaric.zadaca.models.LError;
import hr.foi.uzdiz.antbaric.zadaca.models.LInfo;
import hr.foi.uzdiz.antbaric.zadaca.models.LMessage;
import hr.foi.uzdiz.antbaric.zadaca.models.LNotification;
import hr.foi.uzdiz.antbaric.zadaca.models.LWarning;
import hr.foi.uzdiz.antbaric.zadaca.models.Place;
import hr.foi.uzdiz.antbaric.zadaca.models.Sensor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author javert
 */
public class Worker extends Stateful {

    private static volatile Worker INSTANCE;
    private Configuration CONFIG = null;
    public HashMap<Integer, Place> PLACES;
    public HashMap<Integer, Device> SENSORS;
    public HashMap<Integer, Device> ACTUATORS;
    private HashMap<String, Integer> FAILED_DEVICES;

    static {
        INSTANCE = new Worker();
    }

    private Worker() {
    }

    public static Worker getInstance() {
        return INSTANCE;
    }

    public Configuration getConfig() {
        return CONFIG;
    }

    public void setConfig(Configuration config) {
        CONFIG = config;
    }

    public void run(Integer n) {
        for (Integer i = 0; i < n; i++) {
            Long startTimestamp = System.currentTimeMillis();

            Logger.getInstance().log(new LMessage("Working, interval #" + (i + 1)), true);

            for (Map.Entry<Integer, Place> entry : PLACES.entrySet()) {
                Place place = entry.getValue();
                //List<Device> sensors = this.getSensorsByCategory(place.getCategory());
                Logger.getInstance().log(new LMessage("Checking Place '" + place.getId() + "'"), true);

                for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                    Sensor sensor = (Sensor) deviceIterator.next();

                    if (!this.activateDevice(place.getId(), sensor)) {
                        place.SENSORS_TRASH.add(sensor);
                        deviceIterator.remove();
                        Logger.getInstance().log(new LWarning("Replacing Sensor '" + sensor.getId() + "'"), true);
                        sensor = (Sensor) sensor.prototype(0);

                        if (sensor.getStatus() == 1) {
                            deviceIterator.add(sensor);
                            Logger.getInstance().log(new LMessage("Init OK '" + sensor.getId() + "'"), true);
                        } else {
                            Logger.getInstance().log(new LError("Init FAILED '" + sensor.getId() + "'"), true);
                        }
                    }
                }

                for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                    Actuator actuator = (Actuator) deviceIterator.next();

                    if (actuator.isSensorChanged()) {
                        if (!this.activateDevice(place.getId(), actuator)) {
                            place.ACTUATORS_TRASH.add(actuator);
                            deviceIterator.remove();
                            Logger.getInstance().log(new LMessage("Replacing Actuator '" + actuator.getId() + "'"), true);
                            actuator = (Actuator) actuator.prototype(0);

                            if (actuator.getStatus() == 1) {
                                deviceIterator.add(actuator);
                                Logger.getInstance().log(new LMessage("Init OK '" + actuator.getId() + "'"), true);
                            } else {
                                Logger.getInstance().log(new LError("Init FAILED '" + actuator.getId() + "'"), true);
                            }
                        }
                    } else {
                        Logger.getInstance().log(new LMessage("No change to sensors Actuator '" + actuator.getId() + "'"), true);
                        actuator.activate();
                        FAILED_DEVICES.put(place.getId() + "." + actuator.getId(), 0);
                    }

                }

                entry.setValue(place);

                if (this.isLimitExceeded(startTimestamp)) {
                    Logger.getInstance().log(new LWarning("Thread interval exceeded! Stopping execution"), true);
                    break;
                }
            }
        }

        Logger.getInstance().log(new LInfo("Writing log to output file..."), true);
        Logger.getInstance().writeToFile();
    }

    public void setUp() {
        CSVParser adapter = new CSVParser(CONFIG.getPlacesFilePath(), CONFIG.getActuatorsFielPath(), CONFIG.getSensorsFilePath(), CONFIG.getScheduleFilePath());
        Logger.getInstance().log(new LMessage("Reading CSV files..."), true);
        PLACES = adapter.getPlaces();
        SENSORS = adapter.getSensors();
        ACTUATORS = adapter.getActuators();
        FAILED_DEVICES = new HashMap<>();

        Logger.getInstance().log(new LMessage("Assigning devices..."), true);

        Logger.getInstance().log(new LNotification(String.valueOf(SENSORS.size())), Boolean.TRUE);
        Logger.getInstance().log(new LNotification(String.valueOf(ACTUATORS.size())), Boolean.TRUE);

        adapter.mapDevices(new ArrayList<>(), Boolean.FALSE);

        int count = 0;

        for (Map.Entry<Integer, Place> entry : Worker.getInstance().PLACES.entrySet()) {
            count += entry.getValue().getActuators().size();
            count += entry.getValue().getSensors().size();
        }
        
        Logger.getInstance().log(new LMessage("Init system..."), true);
        this.initSystem();
    }

    public void initSystem() {
        for (Map.Entry<Integer, Place> entry : PLACES.entrySet()) {
            Place place = entry.getValue();
            //List<Device> sensors = this.getSensorsByCategory(place.getCategory());
            Logger.getInstance().log(new LMessage("Init devices at '" + place.getId() + " ID: " + place.getId() + "'"), true);

            for (final ListIterator<Device> deviceIterator = place.getSensors().listIterator(); deviceIterator.hasNext();) {
                final Device sensor = deviceIterator.next();

                if (sensor.getStatus() == 0) {
                    place.SENSORS_TRASH.add(sensor);
                    deviceIterator.remove();
                    Logger.getInstance().log(new LError("  Init FAILED '" + sensor.getId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getId() + "." + sensor.getId(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + sensor.getId() + "'"), true);
                }
            }

            for (final ListIterator<Device> deviceIterator = place.getActuators().listIterator(); deviceIterator.hasNext();) {
                final Device actuator = deviceIterator.next();

                if (actuator.getStatus() == 0) {
                    place.ACTUATORS_TRASH.add(actuator);
                    deviceIterator.remove();
                    Logger.getInstance().log(new LError("  Init FAILED '" + actuator.getId() + "'"), true);
                } else {
                    FAILED_DEVICES.put(place.getId() + "." + actuator.getId(), 0);
                    Logger.getInstance().log(new LMessage("  Init OK '" + actuator.getId() + "'"), true);

                    //this.configActuator((Actuator) actuator, place.getSensors());
                }
            }

            entry.setValue(place);
        }
    }

    private Boolean activateDevice(String placeName, Device device) {
        String deviceId = placeName + "." + device.getId();
        Integer overload = FAILED_DEVICES.get(deviceId);

        if (device.getStatus() == 0) {
            overload = overload == null ? 1 : overload + 1;

            if (overload.equals(3)) {
                FAILED_DEVICES.remove(deviceId);
                Logger.getInstance().log(new LWarning("Device '" + device.getId() + "@'" + placeName + "' need replacement..."), true);

                return false;
            }
        } else {
            overload = 0;
            device.activate();
        }

        FAILED_DEVICES.put(deviceId, overload);

        return true;
    }

    private Boolean isLimitExceeded(Long startTimestamp) {
        return System.currentTimeMillis() - startTimestamp > CONFIG.getPreciseInterval();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object save() {
        return new State((HashMap<Integer, Place>) Worker.copy(this.PLACES));
    }

    @Override
    public void restore(Object o) {
        if (o instanceof State) {
            State s = (State) o;
            INSTANCE.PLACES = ((State) o).state;
        }
    }

    private static class State {

        private final HashMap<Integer, Place> state;

        public State(HashMap<Integer, Place> state) {
            this.state = state;
        }

        public HashMap<Integer, Place> getSavedState() {
            return state;
        }
    }

    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(orig);
                out.flush();
            }

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getInstance().log(new LError(e.getMessage()), Boolean.TRUE);
        }

        return obj;
    }
}
